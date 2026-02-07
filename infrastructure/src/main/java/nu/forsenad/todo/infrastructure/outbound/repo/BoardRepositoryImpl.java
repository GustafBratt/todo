package nu.forsenad.todo.infrastructure.outbound.repo;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.infrastructure.outbound.entity.BoardEntity;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final SpringDataBoardRepository jpaRepository;

    public BoardRepositoryImpl(SpringDataBoardRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Board board) {
        BoardEntity entity = BoardEntity.fromDomain(board);
        jpaRepository.save(entity);
    }

    @Override
    public boolean exists(String boardId) {
        return jpaRepository.existsById(boardId);
    }

    @Override
    public Board findById(String boardId) {
        // Step 1: Fetch board with lists
        Optional<BoardEntity> boardOpt = jpaRepository.findBoardWithListsById(boardId);
        if (boardOpt.isEmpty()) {
            throw new IllegalArgumentException("Board not found: " + boardId);
        }

        // Step 2: Fetch todos for all lists
        BoardEntity board = jpaRepository.fetchTodosForBoard(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found: " + boardId));

        return board.toDomain();
    }

    @Override
    public List<Board> findAll() {
        // Step 1: Fetch all boards with their lists
        List<BoardEntity> boards = jpaRepository.findAllWithLists();

        // Step 2: Fetch todos for each board
        List<BoardEntity> fullyLoadedBoards = boards.stream()
                .map(board -> jpaRepository.fetchTodosForBoard(board.getId())
                        .orElse(board))
                .toList();

        return fullyLoadedBoards.stream()
                .map(BoardEntity::toDomain)
                .toList();
    }

    @Override
    public Board findBoardByListId(String listId) {
        // Step 1: Fetch board with lists
        Optional<BoardEntity> boardOpt = jpaRepository.findBoardWithListsByListId(listId);
        if (boardOpt.isEmpty()) {
            throw new IllegalArgumentException("Board not found for list: " + listId);
        }

        // Step 2: Fetch todos for all lists
        BoardEntity board = jpaRepository.fetchTodosForBoard(boardOpt.get().getId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found for list: " + listId));

        return board.toDomain();
    }

    @Override
    public Board findBoardByTodoId(String todoId) {
        // Step 1: Fetch board with lists
        Optional<BoardEntity> boardOpt = jpaRepository.findBoardWithListsByTodoId(todoId);
        if (boardOpt.isEmpty()) {
            throw new IllegalArgumentException("Board not found for todo: " + todoId);
        }

        // Step 2: Fetch todos for all lists
        BoardEntity board = jpaRepository.fetchTodosForBoard(boardOpt.get().getId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found for todo: " + todoId));

        return board.toDomain();
    }
}

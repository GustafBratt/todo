package nu.forsenad.todo.infrastructure.outbound.repo;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.infrastructure.outbound.entity.BoardEntity;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return jpaRepository.findByIdWithLists(boardId)
                .map(BoardEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Board not found: " + boardId));
    }

    @Override
    public List<Board> findAll() {
        return jpaRepository.findAll().stream()
                .map(BoardEntity::toDomain)
                .toList();
    }

    @Override
    public Board findBoardByListId(String listId) {
        return jpaRepository.findBoardByListId(listId)
                .map(BoardEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Board not found for list: " + listId));
    }
}
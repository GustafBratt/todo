package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.IdGenerator;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.ports.out.BoardRepository;

public class CreateListUseCase {
    private final BoardRepository boardRepository;
    private final IdGenerator idGenerator;


    public CreateListUseCase(BoardRepository boardRepository, IdGenerator idGenerator) {
        this.boardRepository = boardRepository;
        this.idGenerator = idGenerator;
    }

    public Board execute(String boardId, String name) {
        Board board = boardRepository.findById(boardId);

        TodoList newList = TodoList.create(name, idGenerator);
        Board updatedBoard = board.withNewList(newList);

        boardRepository.save(updatedBoard);
        return updatedBoard;
    }
}

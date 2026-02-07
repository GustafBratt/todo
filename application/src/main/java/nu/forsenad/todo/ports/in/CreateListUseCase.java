package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.ports.out.BoardRepository;

public class CreateListUseCase {
    private final BoardRepository boardRepository;

    public CreateListUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String boardId, String name) {
        Board board = boardRepository.findById(boardId);

        TodoList newList = TodoList.create(name);
        Board updatedBoard = board.withNewList(newList);

        boardRepository.save(updatedBoard);
        return updatedBoard;
    }
}

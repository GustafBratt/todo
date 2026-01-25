package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.ports.out.BoardRepository;

public class CreateTodoUseCase {
    private final BoardRepository boardRepository;

    public CreateTodoUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String listId, String title, String description) {
        Board existingBoard = boardRepository.findBoardByListId(listId);
        TodoList existingList = existingBoard.getList(listId);
        TodoList newList = existingList.withNewTodo(title, description);
        Board newBoard = existingBoard.withReplacedList(newList);
        boardRepository.save(newBoard);
        return newBoard;
    }
}

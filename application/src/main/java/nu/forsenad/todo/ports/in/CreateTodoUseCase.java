package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.IdGenerator;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.ports.out.BoardRepository;

public class CreateTodoUseCase {
    private final BoardRepository boardRepository;
    private final IdGenerator idGenerator;


    public CreateTodoUseCase(BoardRepository boardRepository, IdGenerator idGenerator) {
        this.boardRepository = boardRepository;
        this.idGenerator = idGenerator;
    }

    public Board execute(String listId, String title, String description) {
        Board existingBoard = boardRepository.findBoardByListId(listId);
        TodoList existingList = existingBoard.getList(listId);
        TodoList newList = existingList.withNewTodo(title, description, idGenerator);
        Board newBoard = existingBoard.withReplacedList(newList);
        boardRepository.save(newBoard);
        return newBoard;
    }
}

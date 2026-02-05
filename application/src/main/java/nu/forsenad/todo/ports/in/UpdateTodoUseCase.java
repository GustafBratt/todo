package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

public class UpdateTodoUseCase {
    private final BoardRepository boardRepository;

    public UpdateTodoUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String todoId, String title, String description) {
        Board existing = boardRepository.findBoardByTodoId(todoId);
        Board updated = existing.withUpdatedTodo(todoId, title, description);
        boardRepository.save(updated);
        return updated;
    }
}

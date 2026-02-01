package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Todo;
import nu.forsenad.todo.ports.out.TodoRepository;

public class UpdateCardUseCase {
    private final TodoRepository todoRepository;

    public UpdateCardUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo execute(String todoId, String title, String description) {
        Todo existing = todoRepository.findTodoByid(todoId);
        Todo updated = existing.withNewFields(title, description);
        todoRepository.save(updated);
        return updated;
    }
}

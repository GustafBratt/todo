package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.ports.out.ListRepository;

public class UpdateListTitleUseCase {
    private final ListRepository listRepository;

    public UpdateListTitleUseCase(
            ListRepository listRepository
    ) {
        this.listRepository = listRepository;
    }

    public TodoList execute(String listId, String newTitle) {
        TodoList existingList = listRepository.findById(listId);

        TodoList updated = existingList.withNewTitle(newTitle);

        listRepository.save(updated);
        return updated;
    }
}
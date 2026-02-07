package nu.forsenad.todo.domain;

import nu.forsenad.todo.exception.BusinessRuleViolationException;

import java.util.UUID;

public final class Todo {
    private final String id;
    private final String title;
    private final String description;

    public Todo(String id, String title, String description) {
        if (id == null || id.isBlank()) {
            throw new BusinessRuleViolationException("Todo id cannot be blank");
        }
        if (title == null || title.isBlank()) {
            throw new BusinessRuleViolationException("Todo title cannot be blank");
        }
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static Todo create(String title, String description, IdGenerator idGenerator) {
        String id = idGenerator.generateId();
        return new Todo(id, title, description);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Todo withNewFields(String title, String description) {
        return new Todo(this.id, title, description);
    }
}
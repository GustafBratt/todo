package nu.forsenad.todo.infrastructure.inbound.rest.model;

import nu.forsenad.todo.domain.Todo;

class TodoModel {
    private String id;
    private String title;
    private String description;

    public TodoModel(Todo domainTodo) {
        this.id = domainTodo.getId();
        this.title = domainTodo.getTitle();
        this.description = domainTodo.getDescription();
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
}
package nu.forsenad.todo.infrastructure.inbound.rest.model;

import nu.forsenad.todo.domain.TodoList;

import java.util.List;

public class TodoListModel {
    private String id;
    private String title;
    private List<TodoModel> todos;

    public TodoListModel(TodoList domainList) {
        this.id = domainList.getId();
        this.title = domainList.getTitle();
        this.todos = domainList.getTodos().stream()
                .map(TodoModel::new)
                .toList();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<TodoModel> getTodos() {
        return todos;
    }
}
package nu.forsenad.todo.domain;

import nu.forsenad.todo.exception.BusinessRuleViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TodoList {
    private final String id;
    private final String title;
    private final List<Todo> todos;

    public TodoList(String id, String title, List<Todo> todos) {
        if (id == null || id.isBlank()) {
            throw new BusinessRuleViolationException("TodoList id cannot be blank");
        }
        if (title == null || title.isBlank()) {
            throw new BusinessRuleViolationException("TodoList title cannot be blank");
        }
        if (todos == null) {
            throw new BusinessRuleViolationException("Todos cannot be null");
        }
        this.id = id;
        this.title = title;
        this.todos = List.copyOf(todos);
    }

    public static TodoList create(String title, IdGenerator idGenerator) {
        return new TodoList(idGenerator.generateId(), title, List.of());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Todo> getTodos() {
        return List.copyOf(todos);
    }

    public TodoList withTitle(String newTitle) {
        return new TodoList(this.id, newTitle, this.todos);
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", todos=" + todos +
                '}';
    }

    //TODO throw exception if not exactly one match
    List<Todo> withUpdatedTodo(String todoId, String title, String description) {
        return todos.stream()
                .map(todo -> todo.getId().equals(todoId)
                        ? todo.withNewFields(title, description)
                        : todo)
                .toList();
    }

    public TodoList withNewTodo(String title, String description, IdGenerator idGenerator) {
        List<Todo> newList = new ArrayList<>(getTodos());
        newList.add(Todo.create(title, description, idGenerator));
        return new TodoList(this.id, this.title, newList);
    }

}
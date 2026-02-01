package nu.forsenad.todo.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoList {
    private final String id;
    private final String title;
    private final List<Todo> todos;

    public TodoList(String id, String title, List<Todo> todos) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("TodoList id cannot be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("TodoList title cannot be blank");
        }
        if (todos == null) {
            throw new IllegalArgumentException("Todos cannot be null");
        }
        this.id = id;
        this.title = title;
        this.todos = List.copyOf(todos);
    }

    public static TodoList create(String id, String title) {
        return new TodoList(id, title, List.of());
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
        return create(this.id, newTitle);
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", todos=" + todos +
                '}';
    }

    public TodoList withNewTodo(String title, String description) {
        List<Todo> newList = new ArrayList<>(getTodos());
        newList.add(Todo.create(title, description));
        return new TodoList(this.id, this.title, newList);
    }

    public TodoList withNewTitle(String newTitle) {
        return new TodoList(this.id, newTitle, this.todos);
    }
}
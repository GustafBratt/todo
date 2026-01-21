package nu.forsenad.todo.domain;

import java.util.List;

public class Board {
    private final String id;
    private final String name;
    private final List<TodoList> lists;

    public Board(String id, String name, List<TodoList> lists) {
        if (lists == null) {
            throw new IllegalArgumentException("Lists cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Board name cannot be blank");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Board id cannot be blank");
        }
        this.id = id;
        this.name = name;
        this.lists = List.copyOf(lists);
    }

    public static Board create(String id, String name) {
        return new Board(id, name, List.of());
    }

    // Immutable update method - returns new instance with updated name
    public Board withName(String newName) {
        return new Board(this.id, newName, this.lists);
    }

    public Board withLists(List<TodoList> newLists) {
        return new Board(this.id, this.name, newLists);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TodoList> getLists() {
        return lists;
    }
}
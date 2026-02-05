package nu.forsenad.todo.domain;

import java.util.ArrayList;
import java.util.List;

public final class Board {
    private final String id;
    private final String title;
    private final List<TodoList> lists;

    public Board(String id, String title, List<TodoList> lists) {
        if (lists == null) {
            throw new IllegalArgumentException("Lists cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Board title cannot be blank");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Board id cannot be blank");
        }
        this.id = id;
        this.title = title;
        this.lists = List.copyOf(lists);
    }

    public static Board create(String id, String title) {
        return new Board(id, title, List.of());
    }

    // Immutable update method - returns new instance with updated title
    public Board withName(String newName) {
        return new Board(this.id, newName, this.lists);
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return title;
    }

    public List<TodoList> getLists() {
        return new ArrayList<>(lists);
    }

    public Board withNewList(TodoList newList) {
        List<TodoList> newLists = new ArrayList<>(this.lists);
        newLists.add(newList);
        return new Board(this.id, this.title, newLists);
    }

    public Board withDeletedList(String listId) {
        List<TodoList> newLists = new ArrayList<>(this.lists);
        newLists = newLists.stream().filter(tl -> !tl.getId().equals(listId)).toList();
        return new Board(this.id, this.title, newLists);
    }


    public Board withRenamedList(String listId, String newTitle) {
        List<TodoList> newLists = lists.stream()
                .map(list ->
                        list.getId().equals(listId)
                                ? list.withTitle(newTitle)
                                : list
                )
                .toList();

        return new Board(this.id, this.title, newLists);
    }

    public Board withMovedList(String listId, int newPosition) {
        if (newPosition < 0 || newPosition >= lists.size()) {
            throw new IllegalArgumentException("Can't move to position " + newPosition);
        }

        int currentIndex = indexOfList(listId);

        if (currentIndex == newPosition) {
            return this; // no-op, still immutable
        }

        List<TodoList> newLists = new ArrayList<>(lists);
        TodoList list = newLists.remove(currentIndex);
        newLists.add(newPosition, list);

        return new Board(this.id, this.title, newLists);
    }

    private int indexOfList(String listId) {
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId().equals(listId)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Board " + this.id + " has no list with id " + listId);
    }

    public TodoList getList(String listId) {
        return lists.stream().filter(l -> l.getId().equals(listId)).findFirst().get();
    }

    public Board withReplacedList(TodoList newList) {
        var existingLists = new ArrayList<>(getLists());
        List<TodoList> newLists = existingLists.stream()
                .map(l -> {
                    if (l.getId().equals(newList.getId()))
                        return newList;
                    else
                        return l;
                }).toList();
        return new Board(this.id, this.title, newLists);
    }

    public Board withUpdatedTodo(String todoId, String title, String description) {
        List<TodoList> newLists = lists.stream()
                .map(list -> {
                    // Check if this list contains the todo
                    boolean hasTodo = list.getTodos().stream()
                            .anyMatch(todo -> todo.getId().equals(todoId));

                    if (hasTodo) {
                        // Update the todo in this list
                        List<Todo> updatedTodos = list.getTodos().stream()
                                .map(todo -> todo.getId().equals(todoId)
                                        ? todo.withNewFields(title, description)
                                        : todo)
                                .toList();
                        return new TodoList(list.getId(), list.getTitle(), updatedTodos);
                    } else {
                        // Return the list unchanged
                        return list;
                    }
                })
                .toList();

        return new Board(this.id, this.title, newLists);
    }
}
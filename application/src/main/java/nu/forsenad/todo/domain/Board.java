package nu.forsenad.todo.domain;

import nu.forsenad.todo.exception.BusinessRuleViolationException;
import nu.forsenad.todo.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Board {
    private final String id;
    private final String title;
    private final List<TodoList> lists;

    public Board(String id, String title, List<TodoList> lists) {
        if (lists == null) {
            throw new BusinessRuleViolationException("Lists cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new BusinessRuleViolationException("Board title cannot be blank");
        }
        if (id == null || id.isBlank()) {
            throw new BusinessRuleViolationException("Board id cannot be blank");
        }
        this.id = id;
        this.title = title;
        this.lists = List.copyOf(lists);
    }

    // Updated Board.create method to use IdGenerator
    public static Board create(String title, IdGenerator idGenerator) {
        String id = idGenerator.generateId();
        return new Board(id, title, List.of());
    }

    // Immutable update method - returns new instance with updated title
    public Board newTitle(String newTitle) {
        return new Board(this.id, newTitle, this.lists);
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
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
        // Count how many lists match the listId
        long matchCount = lists.stream()
                .filter(list -> list.getId().equals(listId))
                .count();

        if (matchCount == 0) {
            throw new ResourceNotFoundException("list", listId);
        }

        if (matchCount > 1) {
            // This indicates a data integrity issue - should result in HTTP 500
            throw new IllegalStateException(
                    "Data integrity violation: Board " + this.id + " has multiple lists with id " + listId
            );
        }

        // Now perform the deletion - we know exactly one match exists
        List<TodoList> newLists = lists.stream()
                .filter(list -> !list.getId().equals(listId))
                .toList();

        return new Board(this.id, this.title, newLists);
    }

    public Board withRenamedList(String listId, String newTitle) {
        // First, verify that exactly one list matches the listId
        long matchCount = lists.stream()
                .filter(list -> list.getId().equals(listId))
                .count();

        if (matchCount == 0) {
            throw new ResourceNotFoundException("list", listId);
        }

        if (matchCount > 1) {
            // This indicates a data integrity issue - should result in HTTP 500
            throw new IllegalStateException(
                    "Data integrity violation: Board " + this.id + " has multiple lists with id " + listId
            );
        }

        // Now perform the rename - we know exactly one match exists
        List<TodoList> newLists = lists.stream()
                .map(list ->
                        list.getId().equals(listId)
                                ? list.withTitle(newTitle)
                                : list
                )
                .toList();

        return new Board(this.id, this.title, newLists);
    }


    //0-based index for newPosition
    public Board withMovedList(String listId, int newPosition) {
        if (newPosition < 0 || newPosition > lists.size()) {
            throw new BusinessRuleViolationException("Can't move list id " + listId + " to position " + newPosition);
        }

        int currentIndex = indexOfList(listId);

        // If moving forward and removing the element reduces the index of the target,
        // adjust the insertion index accordingly.
        if (currentIndex == newPosition || (currentIndex < newPosition && newPosition == lists.size() && currentIndex == lists.size() - 1)) {
            return this; // no-op
        }

        List<TodoList> newLists = new ArrayList<>(lists);
        TodoList list = newLists.remove(currentIndex);

        // If we removed an element before the insertion point, the insertion index shifts left by 1
        int insertionIndex = newPosition;
        if (currentIndex < newPosition) {
            insertionIndex = newPosition - 1;
        }

        newLists.add(insertionIndex, list);

        return new Board(this.id, this.title, newLists);
    }

    private int indexOfList(String listId) {
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId().equals(listId)) {
                return i;
            }
        }
        throw new IllegalStateException("Board " + this.id + " has no list with id " + listId);
    }

    public TodoList getList(String listId) {
        Optional<TodoList> listOptional = lists.stream().filter(l -> l.getId().equals(listId)).findFirst();
        return listOptional.orElseThrow(() -> new ResourceNotFoundException("list", listId));
    }

    //TODO throw exception if not exactly one match
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
                    Optional<Todo> matchingTodo = list.getTodos().stream()
                            .filter(todo -> todo.getId().equals(todoId))
                            .findFirst();

                    return matchingTodo
                            .map(todo -> new TodoList(
                                    list.getId(),
                                    list.getTitle(),
                                    list.withUpdatedTodo(todoId, title, description)))
                            .orElse(list);
                })
                .toList();

        return new Board(this.id, this.title, newLists);
    }

}
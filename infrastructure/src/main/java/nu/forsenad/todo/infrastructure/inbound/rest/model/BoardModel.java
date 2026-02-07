package nu.forsenad.todo.infrastructure.inbound.rest.model;

import nu.forsenad.todo.domain.Board;

import java.util.List;

public class BoardModel {
    String id;
    String name;
    List<TodoListModel> lists;

    public BoardModel(Board domainBoard) {
        this.id = domainBoard.getId();
        this.name = domainBoard.getTitle();
        this.lists = domainBoard.getLists()
                .stream()
                .map(TodoListModel::new)
                .toList();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TodoListModel> getLists() {
        return lists;
    }
}

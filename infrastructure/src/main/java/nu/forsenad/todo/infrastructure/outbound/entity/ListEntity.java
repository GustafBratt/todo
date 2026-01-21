package nu.forsenad.todo.infrastructure.outbound.entity;

import jakarta.persistence.*;
import nu.forsenad.todo.domain.TodoList;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lists")
public class ListEntity {
    @Id
    private String id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoEntity> todos = new ArrayList<>();

    protected ListEntity() {
        // JPA requires no-arg constructor
    }

    public static ListEntity fromDomain(TodoList todoList) {
        ListEntity entity = new ListEntity();
        entity.id = todoList.getId();
        entity.title = todoList.getTitle();
        entity.todos = todoList.getTodos().stream()
                .map(TodoEntity::fromDomain)
                .toList();
        entity.todos.forEach(todo -> todo.setList(entity));
        return entity;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public TodoList toDomain() {
        return new TodoList(
                this.id,
                this.title,
                this.todos.stream()
                        .map(TodoEntity::toDomain)
                        .toList()
        );
    }
}
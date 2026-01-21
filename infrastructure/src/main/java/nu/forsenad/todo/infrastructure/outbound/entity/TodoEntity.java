package nu.forsenad.todo.infrastructure.outbound.entity;

import jakarta.persistence.*;
import nu.forsenad.todo.domain.Todo;

@Entity
@Table(name = "todos")
public class TodoEntity {
    @Id
    private String id;

    private String title;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private ListEntity list;

    protected TodoEntity() {
        // JPA requires no-arg constructor
    }

    public static TodoEntity fromDomain(Todo todo) {
        TodoEntity entity = new TodoEntity();
        entity.id = todo.getId();
        entity.title = todo.getTitle();
        entity.description = todo.getDescription();
        return entity;
    }

    public void setList(ListEntity list) {
        this.list = list;
    }

    public Todo toDomain() {
        return new Todo(
                this.id,
                this.title,
                this.description
        );
    }
}
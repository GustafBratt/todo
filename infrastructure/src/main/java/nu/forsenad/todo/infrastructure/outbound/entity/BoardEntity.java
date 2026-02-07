package nu.forsenad.todo.infrastructure.outbound.entity;

import jakarta.persistence.*;
import nu.forsenad.todo.domain.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "boards")
public class BoardEntity {

    @Id
    private String id;

    private String name;

    @Version
    private long version;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListEntity> lists = new ArrayList<>();

    public BoardEntity() {
        // JPA requires a no-arg constructor
    }

    // Map from domain to entity
    public static BoardEntity fromDomain(Board board) {
        BoardEntity entity = new BoardEntity();
        entity.id = board.getId();
        entity.name = board.getTitle();
        entity.lists = board.getLists().stream()
                .map(ListEntity::fromDomain)
                .collect(Collectors.toList());
        // Set bidirectional relationship
        entity.lists.forEach(list -> list.setBoard(entity));
        return entity;
    }

    // Map from entity to domain
    public Board toDomain() {
        return new Board(
                this.id,
                this.name,
                this.lists.stream()
                        .map(ListEntity::toDomain)
                        .toList()
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getVersion() {
        return version;
    }

    public List<ListEntity> getLists() {
        return lists;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setLists(List<ListEntity> lists) {
        this.lists = lists;
    }
}
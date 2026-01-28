package nu.forsenad.todo.infrastructure.outbound.repo;
import nu.forsenad.todo.infrastructure.outbound.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataBoardRepository extends JpaRepository<BoardEntity, String> {
    @Query("SELECT b FROM BoardEntity b LEFT JOIN FETCH b.lists WHERE b.id = :id")
    Optional<BoardEntity> findByIdWithLists(@Param("id") String id);

    @Query("SELECT b FROM BoardEntity b LEFT JOIN FETCH b.lists l WHERE l.id = :listId")
    Optional<BoardEntity> findBoardByListId(@Param("listId") String listId);
}
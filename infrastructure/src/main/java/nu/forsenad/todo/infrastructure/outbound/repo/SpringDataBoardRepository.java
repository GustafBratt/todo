package nu.forsenad.todo.infrastructure.outbound.repo;
import nu.forsenad.todo.infrastructure.outbound.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataBoardRepository extends JpaRepository<BoardEntity, String> {

    Optional<BoardEntity> findBoardByListId(@Param("listId") String listId);

    Optional<BoardEntity> findBoardByTodoId(@Param("todoId") String todoId);
}
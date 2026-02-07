package nu.forsenad.todo.infrastructure.outbound.repo;

import nu.forsenad.todo.infrastructure.outbound.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataBoardRepository extends JpaRepository<BoardEntity, String> {

    // ========== STEP 1 QUERIES: Fetch board with lists ==========

    /**
     * Step 1: Find board by list ID and fetch all lists (but not todos yet).
     */
    @Query("SELECT DISTINCT b FROM BoardEntity b " +
            "LEFT JOIN FETCH b.lists l " +
            "WHERE l.id = :listId")
    Optional<BoardEntity> findBoardWithListsByListId(@Param("listId") String listId);

    /**
     * Step 1: Find board by todo ID and fetch all lists (but not todos yet).
     */
    @Query("SELECT DISTINCT b FROM BoardEntity b " +
            "LEFT JOIN FETCH b.lists l " +
            "LEFT JOIN l.todos t " +
            "WHERE t.id = :todoId")
    Optional<BoardEntity> findBoardWithListsByTodoId(@Param("todoId") String todoId);

    /**
     * Step 1: Find board by ID and fetch all lists (but not todos yet).
     */
    @Query("SELECT DISTINCT b FROM BoardEntity b " +
            "LEFT JOIN FETCH b.lists " +
            "WHERE b.id = :boardId")
    Optional<BoardEntity> findBoardWithListsById(@Param("boardId") String boardId);

    /**
     * Step 1: Find all boards and fetch their lists (but not todos yet).
     */
    @Query("SELECT DISTINCT b FROM BoardEntity b " +
            "LEFT JOIN FETCH b.lists")
    java.util.List<BoardEntity> findAllWithLists();

    // ========== STEP 2 QUERIES: Fetch todos for lists ==========

    /**
     * Step 2: Fetch all todos for the lists of the given board.
     * This must be called after a Step 1 query to complete the eager loading.
     */
    @Query("SELECT DISTINCT b FROM BoardEntity b " +
            "JOIN FETCH b.lists l " +
            "LEFT JOIN FETCH l.todos " +
            "WHERE b.id = :boardId")
    Optional<BoardEntity> fetchTodosForBoard(@Param("boardId") String boardId);
}

package nu.forsenad.todo.domain;

import nu.forsenad.todo.exception.BusinessRuleViolationException;
import nu.forsenad.todo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

//Test things not tested by use case test.
class BoardTest {

    @Test
    void constructor_valides_input() {
        assertThrows(BusinessRuleViolationException.class, () -> new Board(null, "Title", List.of()));
        assertThrows(BusinessRuleViolationException.class, () -> new Board("", "Title", List.of()));
        assertThrows(BusinessRuleViolationException.class, () -> new Board(" ", "Title", List.of()));
        assertThrows(BusinessRuleViolationException.class, () -> new Board("id", null, List.of()));
        assertThrows(BusinessRuleViolationException.class, () -> new Board("id", "", List.of()));
        assertThrows(BusinessRuleViolationException.class, () -> new Board("id", " ", List.of()));
        assertThrows(BusinessRuleViolationException.class, () -> new Board("id", "Title", null));
    }

    @Test
    void throws_illegal_state_exception_when_duplicate_list_ids_exist() {
        // Use a FixedIdGenerator that returns the same ID twice
        IdGenerator duplicateIdGenerator = FixedIdGenerator.alwaysReturn("duplicate-id");

        // Create two lists with the same ID
        TodoList list1 = TodoList.create("List 1", duplicateIdGenerator);
        TodoList list2 = TodoList.create("List 2", duplicateIdGenerator);

        // Verify they have the same ID (data corruption scenario)
        assertThat(list1.getId()).isEqualTo(list2.getId());

        // Create a board with duplicate list IDs
        Board board = new Board("b1", "Board 1", java.util.List.of(list1, list2));

        // Attempting to delete should throw IllegalStateException
        assertThatThrownBy(() -> board.withDeletedList("duplicate-id"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Data integrity violation")
                .hasMessageContaining("duplicate-id");
    }

    @Test
    void throws_resource_not_found_when_list_does_not_exist() {
        IdGenerator idGenerator = new SequentialIdGenerator("list-");

        TodoList list1 = TodoList.create("List 1", idGenerator);
        TodoList list2 = TodoList.create("List 2", idGenerator);

        Board board = new Board("b1", "Board 1", java.util.List.of(list1, list2));

        assertThatThrownBy(() -> board.withDeletedList("nonexistent-id"))
                .isInstanceOf(nu.forsenad.todo.exception.ResourceNotFoundException.class)
                .hasMessageContaining("nonexistent-id");
    }

    @Test
    void successfully_deletes_list_when_exactly_one_match() {
        IdGenerator idGenerator = new SequentialIdGenerator("list-");

        TodoList list1 = TodoList.create("List 1", idGenerator);
        TodoList list2 = TodoList.create("List 2", idGenerator);
        TodoList list3 = TodoList.create("List 3", idGenerator);

        Board board = new Board("b1", "Board 1", java.util.List.of(list1, list2, list3));

        Board result = board.withDeletedList(list2.getId());

        assertThat(result.getLists()).hasSize(2);
        assertThat(result.getLists()).containsExactly(list1, list3);
    }

    @Test
    void can_delete_last_remaining_list() {
        IdGenerator idGenerator = new SequentialIdGenerator("list-");

        TodoList list1 = TodoList.create("Only List", idGenerator);

        Board board = new Board("b1", "Board 1", java.util.List.of(list1));

        Board result = board.withDeletedList(list1.getId());

        assertThat(result.getLists()).isEmpty();
    }


    @Test
    void throws_when_moving_nonexistent_list() {
        IdGenerator idGenerator = new SequentialIdGenerator("list-");

        TodoList list1 = TodoList.create("List 1", idGenerator);

        Board board = new Board("b1", "Board 1", java.util.List.of(list1));

        assertThatThrownBy(() -> board.withMovedList("nonexistent-id", 0))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("nonexistent-id");
    }

}

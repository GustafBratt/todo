package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.IdGenerator;
import nu.forsenad.todo.domain.SequentialIdGenerator;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.exception.BusinessRuleViolationException;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoveListWithinBoardUseCaseTest {

    @Mock
    BoardRepository boardRepository;

    MoveListWithinBoardUseCase sut;

    TodoList l1;
    TodoList l2;
    Board board;

    @BeforeEach
    void setUp() {
        sut = new MoveListWithinBoardUseCase(boardRepository);

        l1 = new TodoList("l1", "List 1", new ArrayList<>());
        l2 = new TodoList("l2", "List 2", new ArrayList<>());

        board = new Board("b1", "Board 1", new ArrayList<>())
                .withNewList(l1)
                .withNewList(l2);
    }

    @Test
    void moves_list_to_new_position() {
        IdGenerator idGenerator = new SequentialIdGenerator("list-");
        TodoList a = TodoList.create("A", idGenerator);
        TodoList b = TodoList.create("B", idGenerator);
        TodoList c = TodoList.create("C", idGenerator);

        Board board = new Board("b1", "Board", java.util.List.of(a, b, c));

        // Append A to the end (insert semantics allow position == size)
        Board moved = board.withMovedList(a.getId(), board.getLists().size());
        assertThat(moved.getLists()).containsExactly(b, c, a);

        // Move C (currently at index 1) to the front
        Board moved2 = moved.withMovedList(c.getId(), 0);
        assertThat(moved2.getLists()).containsExactly(c, b, a);

        // Move B (currently at index 1) to the end (position == size)
        Board moved3 = moved2.withMovedList(b.getId(), moved2.getLists().size());
        assertThat(moved3.getLists()).containsExactly(c, a, b);
    }

    @Test
    void moving_list_to_same_position_returns_same_order() {
        when(boardRepository.findBoardByListId("l1"))
                .thenReturn(board);

        Board result = sut.execute("l1", 0);

        assertThat(result.getLists())
                .extracting(TodoList::getId)
                .containsExactly("l1", "l2");

        verify(boardRepository).save(result);
    }
    @Test
    void moving_list_to_negative_position_throws() {
        when(boardRepository.findBoardByListId("l1"))
                .thenReturn(board);

        assertThatThrownBy(() -> sut.execute("l1", -1))
                .isInstanceOf(BusinessRuleViolationException.class);

        verify(boardRepository, never()).save(any());
    }

    @Test
    void moving_list_past_end_throws() {
        IdGenerator idGenerator = new SequentialIdGenerator("list-");
        TodoList list1 = TodoList.create("List 1", idGenerator);
        TodoList list2 = TodoList.create("List 2", idGenerator);

        Board board = new Board("b1", "Board 1", java.util.List.of(list1, list2));

        int pastEndIndex = board.getLists().size() + 1; // strictly greater than size

        assertThatThrownBy(() -> board.withMovedList(list1.getId(), pastEndIndex))
                .isInstanceOf(nu.forsenad.todo.exception.BusinessRuleViolationException.class)
                .hasMessageContaining("Can't move list id");
    }

    @Test
    void moving_unknown_list_throws() {
        when(boardRepository.findBoardByListId("l3"))
                .thenThrow(new RuntimeException("l3"));

        assertThatThrownBy(() -> sut.execute("l3", 0))
                .isInstanceOf(RuntimeException.class);

        verify(boardRepository, never()).save(any());
    }


}

package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
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

    @ParameterizedTest
    @CsvSource({
            "l2, 0, l2, l1",
            "l1, 1, l2, l1"
    })
    void moves_list_to_new_position(
            String listId,
            int newPosition,
            String firstExpected,
            String secondExpected
    ) {
        when(boardRepository.findBoardByListId(listId))
                .thenReturn(board);

        Board result = sut.execute(listId, newPosition);

        assertThat(result.getLists())
                .extracting(TodoList::getId)
                .containsExactly(firstExpected, secondExpected);

        verify(boardRepository).save(result);
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
        when(boardRepository.findBoardByListId("l1"))
                .thenReturn(board);

        assertThatThrownBy(() -> sut.execute("l1", 2))
                .isInstanceOf(BusinessRuleViolationException.class);

        verify(boardRepository, never()).save(any());
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

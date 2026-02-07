package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.domain.UuidIdGenerator;
import nu.forsenad.todo.exception.ResourceNotFoundException;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateListTitleUseCaseTest {

    @Mock
    BoardRepository boardRepository;

    UpdateListTitleUseCase sut;

    TodoList l1;
    TodoList l2;
    String l1Id;
    String l2Id;
    Board board;

    @BeforeEach
    void setUp() {
        sut = new UpdateListTitleUseCase(boardRepository);

        l1 = TodoList.create("List 1", new UuidIdGenerator());
        l2 = TodoList.create("List 2", new UuidIdGenerator());
        l1Id = l1.getId();
        l2Id = l2.getId();

        board = new Board("b1", "Board 1", new ArrayList<>())
                .withNewList(l1)
                .withNewList(l2);
    }

    @Test
    public void list_title_is_updated() {
        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(board);

        Board actual = sut.execute(l1Id, "New Title");

        assertThat(actual.getLists()).hasSize(2);
        assertThat(actual.getLists().get(0).getTitle()).isEqualTo("New Title");
        assertThat(actual.getLists().get(1).getTitle()).isEqualTo("List 2");
    }

    @Test
    public void other_lists_remain_unchanged() {
        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(board);

        Board actual = sut.execute(l1Id, "New Title");

        assertThat(actual.getLists().get(1).getId()).isEqualTo(l2Id);
        assertThat(actual.getLists().get(1).getTitle()).isEqualTo("List 2");
    }

    @Test
    public void updated_board_is_saved() {
        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(board);

        sut.execute(l1Id, "New Title");

        verify(boardRepository).save(any(Board.class));
    }

    @Test
    public void throws_exception_when_list_not_found() {
        when(boardRepository.findBoardByListId("nonexistent-id"))
                .thenReturn(board);

        assertThatThrownBy(() -> sut.execute("nonexistent-id", "New Title"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("nonexistent-id");
    }

    @Test
    public void returns_updated_board() {
        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(board);

        Board result = sut.execute(l1Id, "New Title");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("b1");
        assertThat(result.getTitle()).isEqualTo("Board 1");
    }
}
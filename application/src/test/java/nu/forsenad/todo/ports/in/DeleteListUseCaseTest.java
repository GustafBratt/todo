package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.domain.UuidIdGenerator;
import nu.forsenad.todo.exception.ResourceNotFoundException;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteListUseCaseTest {

    @Mock
    BoardRepository boardRepository;

    DeleteListUseCase sut;

    TodoList l1;
    TodoList l2;
    TodoList l3;
    String l1Id;
    String l2Id;
    String l3Id;
    Board board;

    @BeforeEach
    void setUp() {
        sut = new DeleteListUseCase(boardRepository);

        l1 = TodoList.create("List 1", new UuidIdGenerator());
        l2 = TodoList.create("List 2", new UuidIdGenerator());
        l3 = TodoList.create("List 3", new UuidIdGenerator());
        l1Id = l1.getId();
        l2Id = l2.getId();
        l3Id = l3.getId();

        board = new Board("b1", "Board 1", new ArrayList<>())
                .withNewList(l1)
                .withNewList(l2)
                .withNewList(l3);
    }

    @Test
    public void deletes_first_list_successfully() {
        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(board);

        Board actual = sut.execute(l1Id);

        assertThat(actual.getLists()).hasSize(2);
        assertThat(actual.getLists()).containsExactly(l2, l3);
    }

    @Test
    public void deletes_middle_list_successfully() {
        when(boardRepository.findBoardByListId(l2Id))
                .thenReturn(board);

        Board actual = sut.execute(l2Id);

        assertThat(actual.getLists()).hasSize(2);
        assertThat(actual.getLists()).containsExactly(l1, l3);
    }

    @Test
    public void deletes_last_list_successfully() {
        when(boardRepository.findBoardByListId(l3Id))
                .thenReturn(board);

        Board actual = sut.execute(l3Id);

        assertThat(actual.getLists()).hasSize(2);
        assertThat(actual.getLists()).containsExactly(l1, l2);
    }

    @Test
    public void can_delete_only_list_leaving_board_empty() {
        Board boardWithOneList = new Board("b2", "Board 2", new ArrayList<>())
                .withNewList(l1);

        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(boardWithOneList);

        Board actual = sut.execute(l1Id);

        assertThat(actual.getLists()).isEmpty();
    }

    @Test
    public void throws_exception_when_list_not_found() {
        when(boardRepository.findBoardByListId("nonexistent-id"))
                .thenReturn(board);

        assertThatThrownBy(() -> sut.execute("nonexistent-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("nonexistent-id");
    }

    @Test
    public void board_properties_remain_unchanged() {
        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(board);

        Board actual = sut.execute(l1Id);

        assertThat(actual.getId()).isEqualTo("b1");
        assertThat(actual.getTitle()).isEqualTo("Board 1");
    }

    @Test
    public void remaining_lists_are_unchanged() {
        when(boardRepository.findBoardByListId(l2Id))
                .thenReturn(board);

        Board actual = sut.execute(l2Id);

        // Verify l1 and l3 are unchanged
        TodoList remainingL1 = actual.getLists().get(0);
        TodoList remainingL3 = actual.getLists().get(1);

        assertThat(remainingL1.getId()).isEqualTo(l1Id);
        assertThat(remainingL1.getTitle()).isEqualTo("List 1");

        assertThat(remainingL3.getId()).isEqualTo(l3Id);
        assertThat(remainingL3.getTitle()).isEqualTo("List 3");
    }

    @Test
    public void saves_updated_board_to_repository() {
        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(board);

        sut.execute(l1Id);

        ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardCaptor.capture());

        Board savedBoard = boardCaptor.getValue();
        assertThat(savedBoard.getLists()).hasSize(2);
        assertThat(savedBoard.getLists()).doesNotContain(l1);
    }

    @Test
    public void returns_the_updated_board() {
        when(boardRepository.findBoardByListId(l1Id))
                .thenReturn(board);

        Board result = sut.execute(l1Id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(board.getId());
        assertThat(result.getLists()).hasSize(2);
    }

    @Test
    public void maintains_list_order_after_deletion() {
        when(boardRepository.findBoardByListId(l2Id))
                .thenReturn(board);

        Board actual = sut.execute(l2Id);

        assertThat(actual.getLists().get(0).getId()).isEqualTo(l1Id);
        assertThat(actual.getLists().get(1).getId()).isEqualTo(l3Id);
    }

    @Test
    public void throws_illegal_state_exception_when_duplicate_list_ids_exist() {
        // Use a FixedIdGenerator that returns the same ID multiple times
        // to simulate data corruption scenario
        nu.forsenad.todo.domain.IdGenerator duplicateIdGenerator =
                nu.forsenad.todo.domain.FixedIdGenerator.alwaysReturn("duplicate-id");

        TodoList duplicateList1 = TodoList.create("List 1", duplicateIdGenerator);
        TodoList duplicateList2 = TodoList.create("List 2", duplicateIdGenerator);

        Board corruptedBoard = new Board("b1", "Board 1", java.util.List.of(duplicateList1, duplicateList2));

        when(boardRepository.findBoardByListId("duplicate-id"))
                .thenReturn(corruptedBoard);

        assertThatThrownBy(() -> sut.execute("duplicate-id"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Data integrity violation")
                .hasMessageContaining("duplicate-id");
    }
}
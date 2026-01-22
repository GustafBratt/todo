package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBoardUseCaseTest {

    @Mock
    BoardRepository boardRepository;

    @Test
    void should_update_board_name() {
        // Given
        String boardId = "board-123";
        Board existingBoard = Board.create(boardId, "Old Name");
        when(boardRepository.exists(boardId)).thenReturn(true);
        when(boardRepository.findById(boardId)).thenReturn(existingBoard);

        UpdateBoardUseCase sut = new UpdateBoardUseCase(boardRepository);

        // When
        Board result = sut.execute(boardId, "New Name");

        // Then
        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(captor.capture());

        Board saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(boardId);
        assertThat(saved.getName()).isEqualTo("New Name");
        assertThat(saved.getLists()).isEmpty();

        assertThat(result.getName()).isEqualTo("New Name");
    }

    @Test
    void should_reject_nonexistent_board() {
        when(boardRepository.exists("nonexistent")).thenReturn(false);

        UpdateBoardUseCase sut = new UpdateBoardUseCase(boardRepository);

        assertThrows(IllegalArgumentException.class, () ->
                sut.execute("nonexistent", "New Name")
        );
    }

    @Test
    void should_reject_blank_name() {
        String boardId = "board-123";
        Board existingBoard = Board.create(boardId, "Old Name");
        when(boardRepository.exists(boardId)).thenReturn(true);
        when(boardRepository.findById(boardId)).thenReturn(existingBoard);

        UpdateBoardUseCase sut = new UpdateBoardUseCase(boardRepository);

        assertThrows(IllegalArgumentException.class, () ->
                sut.execute(boardId, "  ")
        );
    }

    @Test
    void should_preserve_existing_lists() {
        // Given
        String boardId = "board-123";
        Board existingBoard = new Board(boardId, "Old Name",
                java.util.List.of(new TodoList("a", "b", List.of())));
        when(boardRepository.exists(boardId)).thenReturn(true);
        when(boardRepository.findById(boardId)).thenReturn(existingBoard);

        UpdateBoardUseCase sut = new UpdateBoardUseCase(boardRepository);

        // When
        Board result = sut.execute(boardId, "New Name");

        // Then
        assertThat(result.getLists()).isEqualTo(existingBoard.getLists());
    }
}
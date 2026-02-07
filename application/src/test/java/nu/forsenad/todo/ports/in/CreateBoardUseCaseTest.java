package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.exception.BusinessRuleViolationException;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateBoardUseCaseTest {
    @Mock
    BoardRepository boardRepository;

    @Test
    void should_persist_ok() {
        CreateBoardUseCase sut = new CreateBoardUseCase(boardRepository);

        sut.execute("Test board");

        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(captor.capture());

        Board saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("Test board");
        assertThat(saved.getLists()).isEmpty();
        assertThat(saved.getId()).isNotEmpty();
    }

    @Test
    void should_reject_null_name() {
        CreateBoardUseCase sut = new CreateBoardUseCase(boardRepository);
        Assertions.assertThrows(BusinessRuleViolationException.class, () ->
                sut.execute(null)
        );
    }

    @Test
    void should_reject_blank_name() {
        CreateBoardUseCase sut = new CreateBoardUseCase(boardRepository);
        Assertions.assertThrows(BusinessRuleViolationException.class, () ->
                sut.execute(" ")
        );
    }

}
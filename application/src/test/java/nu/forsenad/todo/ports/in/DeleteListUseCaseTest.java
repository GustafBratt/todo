package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteListUseCaseTest {

    @Mock
    BoardRepository boardRepository;

    DeleteListUseCase sut;

    TodoList l1;
    TodoList l2;
    Board board;

    @BeforeEach
    void setUp() {
        sut = new DeleteListUseCase(boardRepository);

        l1 = TodoList.create("l1", "List 1");
        l2 = TodoList.create("l2", "List 2");

        board = Board.create("b1", "Board 1")
                .withNewList(l1)
                .withNewList(l2);
    }

    @Test
    public void list_is_removed() {
        when(boardRepository.findBoardByListId("l1"))
                .thenReturn(board);
        Board actual = sut.execute("l1");
        assertThat(actual.getLists()).containsExactly(l2);
    }

}
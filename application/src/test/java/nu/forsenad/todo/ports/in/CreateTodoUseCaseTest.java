package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.Todo;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.domain.UuidIdGenerator;
import nu.forsenad.todo.exception.BusinessRuleViolationException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTodoUseCaseTest {

    @Mock
    BoardRepository boardRepository;

    CreateTodoUseCase sut;

    Board board;
    TodoList list;
    String list1Id;

    @BeforeEach
    void setUp() {
        sut = new CreateTodoUseCase(boardRepository, new UuidIdGenerator());

        list = TodoList.create("My List", new UuidIdGenerator());
        list1Id = list.getId();
        board = new Board("board-1", "My Board", new ArrayList<>())
                .withNewList(list);
    }

    @Test
    void should_add_todo_to_list() {
        when(boardRepository.findBoardByListId(list1Id))
                .thenReturn(board);

        Board result = sut.execute(list1Id, "Buy milk", "Get 2% milk from store");

        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(captor.capture());

        Board saved = captor.getValue();
        TodoList updatedList = saved.getList(list1Id);

        assertThat(updatedList.getTodos()).hasSize(1);
        Todo todo = updatedList.getTodos().get(0);
        assertThat(todo.getTitle()).isEqualTo("Buy milk");
        assertThat(todo.getDescription()).isEqualTo("Get 2% milk from store");
        assertThat(todo.getId()).isNotEmpty();
    }

    @Test
    void should_preserve_existing_todos() {
        TodoList listWithTodo = list.withNewTodo("Existing task", "Description", new UuidIdGenerator());
        Board boardWithTodo = board.withReplacedList(listWithTodo);

        when(boardRepository.findBoardByListId(list1Id))
                .thenReturn(boardWithTodo);

        Board result = sut.execute(list1Id, "New task", "New description");

        TodoList updatedList = result.getList(list1Id);
        assertThat(updatedList.getTodos()).hasSize(2);
        assertThat(updatedList.getTodos())
                .extracting(Todo::getTitle)
                .containsExactly("Existing task", "New task");
    }

    @Test
    void should_allow_null_description() {
        when(boardRepository.findBoardByListId(list1Id))
                .thenReturn(board);

        Board result = sut.execute(list1Id, "Task without description", null);

        TodoList updatedList = result.getList(list1Id);
        assertThat(updatedList.getTodos()).hasSize(1);
        assertThat(updatedList.getTodos().get(0).getDescription()).isNull();
    }

    @Test
    void should_reject_null_title() {
        when(boardRepository.findBoardByListId(list1Id))
                .thenReturn(board);

        assertThatThrownBy(() -> sut.execute(list1Id, null, "Description"))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("title");

        verify(boardRepository, never()).save(any());
    }

    @Test
    void should_reject_blank_title() {
        when(boardRepository.findBoardByListId(list1Id))
                .thenReturn(board);

        assertThatThrownBy(() -> sut.execute(list1Id, "  ", "Description"))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("title");

        verify(boardRepository, never()).save(any());
    }

    @Test
    void should_throw_when_list_not_found() {
        when(boardRepository.findBoardByListId("nonexistent-list"))
                .thenThrow(new RuntimeException("List not found"));

        assertThatThrownBy(() ->
                sut.execute("nonexistent-list", "Task", "Description"))
                .isInstanceOf(RuntimeException.class);

        verify(boardRepository, never()).save(any());
    }

    @Test
    void should_preserve_other_lists_on_board() {
        TodoList list2 = TodoList.create("Other List", new UuidIdGenerator());
        String list2Id = list2.getId();
        Board boardWithMultipleLists = board.withNewList(list2);

        when(boardRepository.findBoardByListId(list1Id))
                .thenReturn(boardWithMultipleLists);

        Board result = sut.execute(list1Id, "New task", "Description");

        assertThat(result.getLists()).hasSize(2);
        assertThat(result.getLists())
                .extracting(TodoList::getId)
                .containsExactly(list1Id, list2Id);

        // Only list-1 should have the new todo
        assertThat(result.getList(list1Id).getTodos()).hasSize(1);
        assertThat(result.getList(list2Id).getTodos()).isEmpty();
    }
}
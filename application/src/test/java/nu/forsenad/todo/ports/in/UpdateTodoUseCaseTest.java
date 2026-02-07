package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.Todo;
import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("UpdateCardUseCase Tests")
class UpdateTodoUseCaseTest {

    private BoardRepository boardRepository;
    private UpdateTodoUseCase updateTodoUseCase;

    @BeforeEach
    void setUp() {
        boardRepository = mock(BoardRepository.class);
        updateTodoUseCase = new UpdateTodoUseCase(boardRepository);
    }

    @Test
    @DisplayName("Should update todo with new title and description")
    void shouldUpdateTodoWithNewTitleAndDescription() {
        // Given
        String todoId = "todo-123";
        String newTitle = "Updated Title";
        String newDescription = "Updated Description";

        Todo originalTodo = new Todo(todoId, "Original Title", "Original Description");
        TodoList list = new TodoList("list-1", "My List", List.of(originalTodo));
        Board originalBoard = new Board("board-1", "My Board", List.of(list));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(originalBoard);

        // When
        Board result = updateTodoUseCase.execute(todoId, newTitle, newDescription);

        // Then
        ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardCaptor.capture());

        Board savedBoard = boardCaptor.getValue();
        Todo updatedTodo = savedBoard.getLists().get(0).getTodos().get(0);

        assertThat(updatedTodo.getId()).isEqualTo(todoId);
        assertThat(updatedTodo.getTitle()).isEqualTo(newTitle);
        assertThat(updatedTodo.getDescription()).isEqualTo(newDescription);
        assertThat(result).isEqualTo(savedBoard);
    }

    @Test
    @DisplayName("Should update only the specified todo when multiple todos exist")
    void shouldUpdateOnlySpecifiedTodoWhenMultipleTodosExist() {
        // Given
        String todoId1 = "todo-1";
        String todoId2 = "todo-2";
        String todoId3 = "todo-3";

        Todo todo1 = new Todo(todoId1, "Todo 1", "Description 1");
        Todo todo2 = new Todo(todoId2, "Todo 2", "Description 2");
        Todo todo3 = new Todo(todoId3, "Todo 3", "Description 3");

        TodoList list = new TodoList("list-1", "My List", List.of(todo1, todo2, todo3));
        Board originalBoard = new Board("board-1", "My Board", List.of(list));

        when(boardRepository.findBoardByTodoId(todoId2)).thenReturn(originalBoard);

        // When
        updateTodoUseCase.execute(todoId2, "Updated Todo 2", "Updated Description 2");

        // Then
        ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardCaptor.capture());

        Board savedBoard = boardCaptor.getValue();
        List<Todo> todos = savedBoard.getLists().get(0).getTodos();

        assertThat(todos.get(0).getTitle()).isEqualTo("Todo 1");
        assertThat(todos.get(0).getDescription()).isEqualTo("Description 1");

        assertThat(todos.get(1).getTitle()).isEqualTo("Updated Todo 2");
        assertThat(todos.get(1).getDescription()).isEqualTo("Updated Description 2");

        assertThat(todos.get(2).getTitle()).isEqualTo("Todo 3");
        assertThat(todos.get(2).getDescription()).isEqualTo("Description 3");
    }

    @Test
    @DisplayName("Should update todo in correct list when multiple lists exist")
    void shouldUpdateTodoInCorrectListWhenMultipleListsExist() {
        // Given
        String todoId = "todo-2";

        Todo todo1 = new Todo("todo-1", "Todo 1", "Description 1");
        Todo todo2 = new Todo(todoId, "Todo 2", "Description 2");
        Todo todo3 = new Todo("todo-3", "Todo 3", "Description 3");

        TodoList list1 = new TodoList("list-1", "List 1", List.of(todo1));
        TodoList list2 = new TodoList("list-2", "List 2", List.of(todo2));
        TodoList list3 = new TodoList("list-3", "List 3", List.of(todo3));

        Board originalBoard = new Board("board-1", "My Board", List.of(list1, list2, list3));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(originalBoard);

        // When
        updateTodoUseCase.execute(todoId, "Updated Todo 2", "Updated Description 2");

        // Then
        ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardCaptor.capture());

        Board savedBoard = boardCaptor.getValue();

        // List 1 should be unchanged
        assertThat(savedBoard.getLists().get(0).getTodos().get(0).getTitle()).isEqualTo("Todo 1");

        // List 2 should have the updated todo
        assertThat(savedBoard.getLists().get(1).getTodos().get(0).getTitle()).isEqualTo("Updated Todo 2");
        assertThat(savedBoard.getLists().get(1).getTodos().get(0).getDescription()).isEqualTo("Updated Description 2");

        // List 3 should be unchanged
        assertThat(savedBoard.getLists().get(2).getTodos().get(0).getTitle()).isEqualTo("Todo 3");
    }

    @Test
    @DisplayName("Should preserve board and list structure during update")
    void shouldPreserveBoardAndListStructureDuringUpdate() {
        // Given
        String todoId = "todo-1";
        String boardId = "board-123";
        String boardName = "Important Board";
        String listId = "list-456";
        String listTitle = "Important List";

        Todo todo = new Todo(todoId, "Original", "Original Desc");
        TodoList list = new TodoList(listId, listTitle, List.of(todo));
        Board originalBoard = new Board(boardId, boardName, List.of(list));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(originalBoard);

        // When
        updateTodoUseCase.execute(todoId, "Updated", "Updated Desc");

        // Then
        ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardCaptor.capture());

        Board savedBoard = boardCaptor.getValue();

        assertThat(savedBoard.getId()).isEqualTo(boardId);
        assertThat(savedBoard.getTitle()).isEqualTo(boardName);
        assertThat(savedBoard.getLists()).hasSize(1);
        assertThat(savedBoard.getLists().get(0).getId()).isEqualTo(listId);
        assertThat(savedBoard.getLists().get(0).getTitle()).isEqualTo(listTitle);
    }

    @Test
    @DisplayName("Should call repository findBoardByTodoId with correct todoId")
    void shouldCallRepositoryFindBoardByTodoIdWithCorrectTodoId() {
        // Given
        String todoId = "specific-todo-id";
        Todo todo = new Todo(todoId, "Title", "Description");
        TodoList list = new TodoList("list-1", "List", List.of(todo));
        Board board = new Board("board-1", "Board", List.of(list));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(board);

        // When
        updateTodoUseCase.execute(todoId, "New Title", "New Description");

        // Then
        verify(boardRepository).findBoardByTodoId(todoId);
    }

    @Test
    @DisplayName("Should call repository save exactly once")
    void shouldCallRepositorySaveExactlyOnce() {
        // Given
        String todoId = "todo-1";
        Todo todo = new Todo(todoId, "Title", "Description");
        TodoList list = new TodoList("list-1", "List", List.of(todo));
        Board board = new Board("board-1", "Board", List.of(list));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(board);

        // When
        updateTodoUseCase.execute(todoId, "New Title", "New Description");

        // Then
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("Should return the updated board")
    void shouldReturnTheUpdatedBoard() {
        // Given
        String todoId = "todo-1";
        String newTitle = "New Title";
        String newDescription = "New Description";

        Todo todo = new Todo(todoId, "Old Title", "Old Description");
        TodoList list = new TodoList("list-1", "List", List.of(todo));
        Board originalBoard = new Board("board-1", "Board", List.of(list));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(originalBoard);

        // When
        Board result = updateTodoUseCase.execute(todoId, newTitle, newDescription);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLists().get(0).getTodos().get(0).getTitle()).isEqualTo(newTitle);
        assertThat(result.getLists().get(0).getTodos().get(0).getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("Should throw exception when board is not found for todoId")
    void shouldThrowExceptionWhenBoardNotFoundForTodoId() {
        // Given
        String todoId = "non-existent-todo";
        when(boardRepository.findBoardByTodoId(todoId))
                .thenThrow(new IllegalArgumentException("Board not found for todo: " + todoId));

        // When & Then
        assertThatThrownBy(() -> updateTodoUseCase.execute(todoId, "Title", "Description"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Board not found for todo: " + todoId);

        verify(boardRepository, never()).save(any(Board.class));
    }

    @Test
    @DisplayName("Should maintain immutability - original board should not be modified")
    void shouldMaintainImmutabilityOriginalBoardShouldNotBeModified() {
        // Given
        String todoId = "todo-1";
        String originalTitle = "Original Title";
        String originalDescription = "Original Description";

        Todo todo = new Todo(todoId, originalTitle, originalDescription);
        TodoList list = new TodoList("list-1", "List", List.of(todo));
        Board originalBoard = new Board("board-1", "Board", List.of(list));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(originalBoard);

        // When
        updateTodoUseCase.execute(todoId, "New Title", "New Description");

        // Then - original board should remain unchanged
        assertThat(originalBoard.getLists().get(0).getTodos().get(0).getTitle()).isEqualTo(originalTitle);
        assertThat(originalBoard.getLists().get(0).getTodos().get(0).getDescription()).isEqualTo(originalDescription);
    }

    @Test
    @DisplayName("Should handle empty description")
    void shouldHandleEmptyDescription() {
        // Given
        String todoId = "todo-1";
        String newTitle = "New Title";
        String emptyDescription = "";

        Todo todo = new Todo(todoId, "Old Title", "Old Description");
        TodoList list = new TodoList("list-1", "List", List.of(todo));
        Board board = new Board("board-1", "Board", List.of(list));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(board);

        // When
        Board result = updateTodoUseCase.execute(todoId, newTitle, emptyDescription);

        // Then
        assertThat(result.getLists().get(0).getTodos().get(0).getDescription()).isEqualTo(emptyDescription);
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        // Given
        String todoId = "todo-1";
        String newTitle = "New Title";

        Todo todo = new Todo(todoId, "Old Title", "Old Description");
        TodoList list = new TodoList("list-1", "List", List.of(todo));
        Board board = new Board("board-1", "Board", List.of(list));

        when(boardRepository.findBoardByTodoId(todoId)).thenReturn(board);

        // When
        Board result = updateTodoUseCase.execute(todoId, newTitle, null);

        // Then
        assertThat(result.getLists().get(0).getTodos().get(0).getDescription()).isNull();
    }
}
package nu.forsenad.todo.domain;

import nu.forsenad.todo.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TodoListTest {

    @Test
    void shouldCreateTodoListWithValidParameters() {
        // Given
        String id = "list-123";
        String title = "My Todo List";
        List<Todo> todos = List.of(
                new Todo("todo-1", "First task", "Description 1"),
                new Todo("todo-2", "Second task", "Description 2")
        );

        // When
        TodoList todoList = new TodoList(id, title, todos);

        // Then
        assertThat(todoList.getId()).isEqualTo(id);
        assertThat(todoList.getTitle()).isEqualTo(title);
        assertThat(todoList.getTodos()).hasSize(2);
        assertThat(todoList.getTodos()).containsExactly(todos.toArray(new Todo[0]));
    }

    @Test
    void shouldCreateTodoListWithEmptyTodosList() {
        // Given
        String id = "list-123";
        String title = "Empty List";
        List<Todo> emptyTodos = List.of();

        // When
        TodoList todoList = new TodoList(id, title, emptyTodos);

        // Then
        assertThat(todoList.getId()).isEqualTo(id);
        assertThat(todoList.getTitle()).isEqualTo(title);
        assertThat(todoList.getTodos()).isEmpty();
    }

    @Test
    void shouldCreateDefensiveCopyOfTodosList() {
        // Given
        String id = "list-123";
        String title = "My List";
        List<Todo> mutableTodos = new ArrayList<>();
        mutableTodos.add(new Todo("todo-1", "Task 1", "Desc 1"));

        // When
        TodoList todoList = new TodoList(id, title, mutableTodos);
        mutableTodos.add(new Todo("todo-2", "Task 2", "Desc 2")); // Modify original list

        // Then
        assertThat(todoList.getTodos()).hasSize(1)
                .as("TodoList should not be affected by changes to the original list");
    }

    @Test
    void getTodosShouldReturnDefensiveCopy() {
        // Given
        String id = "list-123";
        String title = "My List";
        List<Todo> todos = List.of(new Todo("todo-1", "Task 1", "Desc 1"));
        TodoList todoList = new TodoList(id, title, todos);

        // When
        List<Todo> retrievedTodos = todoList.getTodos();

        // Then
        assertThatThrownBy(() -> retrievedTodos.add(new Todo("todo-2", "Task 2", "Desc 2")))
                .isInstanceOf(UnsupportedOperationException.class)
                .as("getTodos() should return an unmodifiable list");
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        String title = "My List";
        List<Todo> todos = List.of();

        // When/Then
        assertThatThrownBy(() -> new TodoList(null, title, todos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("TodoList id cannot be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void shouldThrowExceptionWhenIdIsBlank(String blankId) {
        // Given
        String title = "My List";
        List<Todo> todos = List.of();

        // When/Then
        assertThatThrownBy(() -> new TodoList(blankId, title, todos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("TodoList id cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        // Given
        String id = "list-123";
        List<Todo> todos = List.of();

        // When/Then
        assertThatThrownBy(() -> new TodoList(id, null, todos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("TodoList title cannot be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void shouldThrowExceptionWhenTitleIsBlank(String blankTitle) {
        // Given
        String id = "list-123";
        List<Todo> todos = List.of();

        // When/Then
        assertThatThrownBy(() -> new TodoList(id, blankTitle, todos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("TodoList title cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenTodosIsNull() {
        // Given
        String id = "list-123";
        String title = "My List";

        // When/Then
        assertThatThrownBy(() -> new TodoList(id, title, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todos cannot be null");
    }

}
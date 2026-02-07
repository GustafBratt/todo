package nu.forsenad.todo.domain;

import nu.forsenad.todo.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class TodoTest {

    @Test
    void shouldCreateTodoWithValidParameters() {
        // Given
        String id = "todo-123";
        String title = "Buy groceries";
        String description = "Milk, eggs, bread";

        // When
        Todo todo = new Todo(id, title, description);

        // Then
        assertThat(todo.getId()).isEqualTo(id);
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getDescription()).isEqualTo(description);
    }

    @Test
    void shouldCreateTodoWithNullDescription() {
        // Given
        String id = "todo-123";
        String title = "Task without description";

        // When
        Todo todo = new Todo(id, title, null);

        // Then
        assertThat(todo.getId()).isEqualTo(id);
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getDescription()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        String title = "My Task";
        String description = "Description";

        // When/Then
        assertThatThrownBy(() -> new Todo(null, title, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo id cannot be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void shouldThrowExceptionWhenIdIsBlank(String blankId) {
        // Given
        String title = "My Task";
        String description = "Description";

        // When/Then
        assertThatThrownBy(() -> new Todo(blankId, title, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo id cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        // Given
        String id = "todo-123";
        String description = "Description";

        // When/Then
        assertThatThrownBy(() -> new Todo(id, null, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo title cannot be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void shouldThrowExceptionWhenTitleIsBlank(String blankTitle) {
        // Given
        String id = "todo-123";
        String description = "Description";

        // When/Then
        assertThatThrownBy(() -> new Todo(id, blankTitle, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo title cannot be blank");
    }
}
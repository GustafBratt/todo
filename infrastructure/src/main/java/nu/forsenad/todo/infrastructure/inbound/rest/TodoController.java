package nu.forsenad.todo.infrastructure.inbound.rest;

import io.swagger.v3.oas.annotations.Operation;
import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.infrastructure.inbound.rest.model.BoardModel;
import nu.forsenad.todo.infrastructure.inbound.rest.model.CreateTodoRequest;
import nu.forsenad.todo.ports.in.CreateTodoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final CreateTodoUseCase createTodoUseCase;

    public TodoController(CreateTodoUseCase createTodoUseCase) {
        this.createTodoUseCase = createTodoUseCase;
    }

    // POST /todos - Create new todo in a
    @Operation(summary = "Create new todo")
    @PostMapping
    public ResponseEntity<BoardModel> createTodo(@RequestBody CreateTodoRequest request) {
        Board updatedBoard = createTodoUseCase.execute(
                request.getListId(),
                request.getTitle(),
                request.getDescription()
        );
        return ResponseEntity.ok(new BoardModel(updatedBoard));
    }
}
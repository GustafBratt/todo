package nu.forsenad.todo.infrastructure.inbound.rest;

import io.swagger.v3.oas.annotations.Operation;
import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.infrastructure.inbound.rest.model.BoardModel;
import nu.forsenad.todo.infrastructure.inbound.rest.model.CreateListRequest;
import nu.forsenad.todo.infrastructure.inbound.rest.model.MoveListRequest;
import nu.forsenad.todo.infrastructure.inbound.rest.model.UpdateListRequest;
import nu.forsenad.todo.ports.in.CreateListUseCase;
import nu.forsenad.todo.ports.in.DeleteListUseCase;
import nu.forsenad.todo.ports.in.MoveListWithinBoardUseCase;
import nu.forsenad.todo.ports.in.UpdateListTitleUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
public class ListController {

    private final CreateListUseCase createListUseCase;
    private final DeleteListUseCase deleteListUseCase;
    private final UpdateListTitleUseCase updateListTitleUseCase;
    private final MoveListWithinBoardUseCase moveListWithinBoardUseCase;

    public ListController(CreateListUseCase createListUseCase,
                          DeleteListUseCase deleteListUseCase,
                          UpdateListTitleUseCase updateListTitleUseCase,
                          MoveListWithinBoardUseCase moveListWithinBoardUseCase) {
        this.createListUseCase = createListUseCase;
        this.deleteListUseCase = deleteListUseCase;
        this.updateListTitleUseCase = updateListTitleUseCase;
        this.moveListWithinBoardUseCase = moveListWithinBoardUseCase;
    }

    // POST /lists - Create new list on a board
    @Operation(summary = "Create new list on a board")
    @PostMapping
    public ResponseEntity<BoardModel> createList(@RequestBody CreateListRequest request) {
        Board updatedBoard = createListUseCase.execute(request.getBoardId(), request.getName());
        return ResponseEntity.ok(new BoardModel(updatedBoard));
    }

    // DELETE /lists/{id} - Delete a list
    @Operation(summary = "Delete a list")
    @DeleteMapping("/{id}")
    public ResponseEntity<BoardModel> deleteList(@PathVariable String id) {
        Board updatedBoard = deleteListUseCase.execute(id);
        return ResponseEntity.ok(new BoardModel(updatedBoard));
    }

    // PUT /lists/{id} - Update list title
    @Operation(summary="Update list title")
    @PutMapping("/{id}")
    public ResponseEntity<BoardModel> updateListTitle(
            @PathVariable String id,
            @RequestBody UpdateListRequest request) {
        Board updatedBoard = updateListTitleUseCase.execute(id, request.getTitle());
        return ResponseEntity.ok(new BoardModel(updatedBoard));
    }

    // PUT /lists/{id}/move - Move list to new position
    @Operation(summary = "Move a list within a board. Positions are 0-indexed.")
    @PutMapping("/{id}/move")
    public ResponseEntity<BoardModel> moveList(
            @PathVariable String id,
            @RequestBody MoveListRequest request) {
        Board updatedBoard = moveListWithinBoardUseCase.execute(id, request.getPosition());
        return ResponseEntity.ok(new BoardModel(updatedBoard));
    }
}
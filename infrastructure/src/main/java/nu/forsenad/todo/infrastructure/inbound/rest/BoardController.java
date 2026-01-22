package nu.forsenad.todo.infrastructure.inbound.rest;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.infrastructure.inbound.rest.model.BoardModel;
import nu.forsenad.todo.infrastructure.inbound.rest.model.CreateUpdateBoardRequest;
import nu.forsenad.todo.ports.in.CreateBoardUseCase;
import nu.forsenad.todo.ports.in.UpdateBoardUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final CreateBoardUseCase createBoardUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;

    public BoardController(CreateBoardUseCase createBoardUseCase,
                           UpdateBoardUseCase updateBoardUseCase) {
        this.createBoardUseCase = createBoardUseCase;
        this.updateBoardUseCase = updateBoardUseCase;
    }

    // curl -X POST http://localhost:8080/boards -H "Content-Type: application/json" -d '{"name":"My First Board"}'

    @PostMapping
    public ResponseEntity<BoardModel> createBoard(@RequestBody CreateUpdateBoardRequest request) {
        Board newBoard = createBoardUseCase.execute(request.getName());
        return ResponseEntity.ok(new BoardModel(newBoard));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardModel> updateBoard(
            @PathVariable String id,
            @RequestBody CreateUpdateBoardRequest request) {
        Board newBoard = updateBoardUseCase.execute(id, request.getName());
        return ResponseEntity.ok(new BoardModel(newBoard));
    }

}
package nu.forsenad.todo.infrastructure.inbound.rest;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.infrastructure.inbound.rest.model.BoardModel;
import nu.forsenad.todo.infrastructure.inbound.rest.model.CreateBoardRequest;
import nu.forsenad.todo.ports.in.CreateBoardUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final CreateBoardUseCase createBoardUseCase;

    public BoardController(CreateBoardUseCase createBoardUseCase) {
        this.createBoardUseCase = createBoardUseCase;
    }

    // curl -X POST http://localhost:8080/boards -H "Content-Type: application/json" -d '{"name":"My First Board"}'

    @PostMapping
    public ResponseEntity<BoardModel> createBoard(@RequestBody CreateBoardRequest request) {
        Board newBoard = createBoardUseCase.execute(request.getName());
        return ResponseEntity.ok(new BoardModel(newBoard));
    }
}
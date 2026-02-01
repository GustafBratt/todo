package nu.forsenad.todo.infrastructure.inbound.rest;

import io.swagger.v3.oas.annotations.Operation;
import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.infrastructure.inbound.rest.model.BoardModel;
import nu.forsenad.todo.infrastructure.inbound.rest.model.CreateUpdateBoardRequest;
import nu.forsenad.todo.ports.in.CreateBoardUseCase;
import nu.forsenad.todo.ports.in.GetBoardDetailsUseCase;
import nu.forsenad.todo.ports.in.ListAllBoardsUseCase;
import nu.forsenad.todo.ports.in.UpdateBoardUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final CreateBoardUseCase createBoardUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;
    private final GetBoardDetailsUseCase getBoardDetailsUseCase;
    private final ListAllBoardsUseCase listAllBoardsUseCase;

    public BoardController(CreateBoardUseCase createBoardUseCase,
                           UpdateBoardUseCase updateBoardUseCase,
                           GetBoardDetailsUseCase getBoardDetailsUseCase,
                           ListAllBoardsUseCase listAllBoardsUseCase) {
        this.createBoardUseCase = createBoardUseCase;
        this.updateBoardUseCase = updateBoardUseCase;
        this.getBoardDetailsUseCase = getBoardDetailsUseCase;
        this.listAllBoardsUseCase = listAllBoardsUseCase;
    }

    // GET /boards - List all boards
    @GetMapping
    @Operation(summary="List all boards")
    public ResponseEntity<List<BoardModel>> listBoards() {
        List<Board> boards = listAllBoardsUseCase.execute();
        List<BoardModel> models = boards.stream()
                .map(BoardModel::new)
                .toList();
        return ResponseEntity.ok(models);
    }

    // GET /boards/{id} - Get board details
    @Operation(summary = "Get one board")
    @GetMapping("/{id}")
    public ResponseEntity<BoardModel> getBoardDetails(@PathVariable String id) {
        Board board = getBoardDetailsUseCase.execute(id);
        return ResponseEntity.ok(new BoardModel(board));
    }

    // POST /boards - Create new board
    @Operation(summary="Create new board")
    @PostMapping
    public ResponseEntity<BoardModel> createBoard(@RequestBody CreateUpdateBoardRequest request) {
        Board newBoard = createBoardUseCase.execute(request.getName());
        return ResponseEntity.ok(new BoardModel(newBoard));
    }

    // PUT /boards/{id} - Update board
    @Operation(summary = "Change name of board")
    @PutMapping("/{id}")
    public ResponseEntity<BoardModel> updateBoard(
            @PathVariable String id,
            @RequestBody CreateUpdateBoardRequest request) {
        Board updatedBoard = updateBoardUseCase.execute(id, request.getName());
        return ResponseEntity.ok(new BoardModel(updatedBoard));
    }
}
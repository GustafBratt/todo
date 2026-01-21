package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

import java.util.UUID;

public class CreateBoardUseCase {
    private final BoardRepository boardRepository;

    public CreateBoardUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String name) {
        String boardId = UUID.randomUUID().toString();

        Board board = Board.create(
                boardId,
                name
        );

        boardRepository.save(board);

        return board;
    }
}

package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

public class CreateBoardUseCase {
    private final BoardRepository boardRepository;

    public CreateBoardUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String title) {
        Board board = Board.create(
                title
        );

        boardRepository.save(board);

        return board;
    }
}

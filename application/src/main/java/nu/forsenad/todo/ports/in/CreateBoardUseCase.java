package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.domain.IdGenerator;
import nu.forsenad.todo.ports.out.BoardRepository;

public class CreateBoardUseCase {
    private final BoardRepository boardRepository;
    private final IdGenerator idGenerator;


    public CreateBoardUseCase(BoardRepository boardRepository, IdGenerator idGenerator) {
        this.boardRepository = boardRepository;
        this.idGenerator = idGenerator;
    }

    public Board execute(String title) {
        Board board = Board.create(
                title,
                idGenerator
        );

        boardRepository.save(board);

        return board;
    }
}

package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

public class GetBoardDetailsUseCase {
    private final BoardRepository boardRepository;

    public GetBoardDetailsUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String boardId) {
        return boardRepository.findById(boardId);
    }
}

package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

import java.util.List;

public class ListAllBoardsUseCase {
    private final BoardRepository boardRepository;

    public ListAllBoardsUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> execute() {
        return boardRepository.findAll();
    }
}

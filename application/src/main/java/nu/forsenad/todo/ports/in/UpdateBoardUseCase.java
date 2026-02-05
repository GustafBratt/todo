package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

public class UpdateBoardUseCase {
    private final BoardRepository boardRepository;

    public UpdateBoardUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String boardId, String newTitle) {
        if (!boardRepository.exists(boardId)) {
            throw new IllegalArgumentException("Board with id " + boardId + " does not exist");
        }

        Board existingBoard = boardRepository.findById(boardId);
        Board updatedBoard = existingBoard.withName(newTitle);

        boardRepository.save(updatedBoard);

        return updatedBoard;
    }
}
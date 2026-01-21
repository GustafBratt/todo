package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

public class UpdateBoardNameUseCase {
    private final BoardRepository boardRepository;

    public UpdateBoardNameUseCase(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String boardId, String newName) {
        if (!boardRepository.exists(boardId)) {
            throw new IllegalArgumentException("Board with id " + boardId + " does not exist");
        }

        Board existingBoard = boardRepository.findById(boardId);
        Board updatedBoard = existingBoard.withName(newName);

        boardRepository.save(updatedBoard);

        return updatedBoard;
    }
}
package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

public class MoveListUseCase {
    private final BoardRepository boardRepository;

    public MoveListUseCase(
            BoardRepository boardRepository
    ) {
        this.boardRepository = boardRepository;
    }

    //Reorders lists on the board.
    //Positions are 0-indexed
    public Board execute(String listId, int newPosition) {
        Board board = boardRepository.findBoardByListId(listId);

        Board updatedBoard = board.moveList(listId, newPosition);

        boardRepository.save(updatedBoard);
        return updatedBoard;
    }
}

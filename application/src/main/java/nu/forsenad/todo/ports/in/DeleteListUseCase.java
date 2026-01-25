package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

public class DeleteListUseCase {
    private final BoardRepository boardRepository;

    public DeleteListUseCase(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public Board execute(String listId) {
        Board existingBoard = boardRepository.findBoardByListId(listId);
        Board newBoard = existingBoard.withDeletedList(listId);
        boardRepository.save(newBoard);
        return newBoard;
    }
}

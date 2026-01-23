package nu.forsenad.todo.ports.in;

import nu.forsenad.todo.domain.Board;
import nu.forsenad.todo.ports.out.BoardRepository;

public class UpdateListTitleUseCase {
    private final BoardRepository boardRepository;

    public UpdateListTitleUseCase(
            BoardRepository boardRepository
    ) {
        this.boardRepository = boardRepository;
    }

    public Board execute(String listId, String newTitle) {
        Board existingBoard = boardRepository.findBoardByListId(listId);

        Board updated = existingBoard.withRenamedList(listId, newTitle);

        boardRepository.save(updated);
        return updated;
    }
}
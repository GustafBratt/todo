package nu.forsenad.todo.ports.out;

import nu.forsenad.todo.domain.Board;

import java.util.List;

public interface BoardRepository {
    void save(Board board);

    boolean exists(String boardId);

    Board findById(String boardId);

    List<Board> findAll();

    Board findBoardByListId(String listId);
}
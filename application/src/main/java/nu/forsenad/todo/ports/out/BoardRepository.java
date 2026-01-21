package nu.forsenad.todo.ports.out;

import nu.forsenad.todo.domain.Board;

public interface BoardRepository {
    void save(Board board);

    boolean exists(String boardId);

    Board findById(String boardId);
}
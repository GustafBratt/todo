package nu.forsenad.todo.ports.out;

import nu.forsenad.todo.domain.Todo;

import java.util.List;

public interface TodoRepository {
    void save(Todo board);

    boolean exists(String todoId);

    Todo findById(String todoId);

    List<Todo> findAll();
    
    Todo findTodoByid(String todoId);
}
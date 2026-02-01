package nu.forsenad.todo.ports.out;

import nu.forsenad.todo.domain.TodoList;
import nu.forsenad.todo.domain.Todo;

import java.util.List;

public interface ListRepository {
    void save(TodoList list);

    boolean exists(String listId);

    TodoList findById(String listId);

    List<TodoList> findAll();

    TodoList findTodoListByListId(String listId);

    Todo findTodoByid(String todoId);
}
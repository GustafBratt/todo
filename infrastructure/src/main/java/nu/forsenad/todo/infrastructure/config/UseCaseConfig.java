package nu.forsenad.todo.infrastructure.config;

import nu.forsenad.todo.ports.in.*;
import nu.forsenad.todo.ports.out.BoardRepository;
import nu.forsenad.todo.ports.out.ListRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateBoardUseCase createBoardUseCase(BoardRepository boardRepository) {
        return new CreateBoardUseCase(boardRepository);
    }

    @Bean
    public UpdateBoardUseCase updateBoardUseCase(BoardRepository boardRepository) {
        return new UpdateBoardUseCase(boardRepository);
    }

    @Bean
    public ListAllBoardsUseCase listAllBoardsUseCase(BoardRepository boardRepository) {
        return new ListAllBoardsUseCase(boardRepository);
    }

    @Bean
    public GetBoardDetailsUseCase getBoardDetailsUseCase(BoardRepository boardRepository) {
        return new GetBoardDetailsUseCase(boardRepository);
    }

    @Bean
    public CreateListUseCase createListUseCase(BoardRepository boardRepository) {
        return new CreateListUseCase(boardRepository);
    }

    @Bean
    public DeleteListUseCase deleteListUseCase(BoardRepository boardRepository) {
        return new DeleteListUseCase(boardRepository);
    }

    @Bean
    public UpdateListTitleUseCase updateListTitleUseCase(ListRepository listRepository) {
        return new UpdateListTitleUseCase(listRepository);
    }

    @Bean
    public MoveListWithinBoardUseCase moveListWithinBoardUseCase(BoardRepository boardRepository) {
        return new MoveListWithinBoardUseCase(boardRepository);
    }

    @Bean
    public CreateTodoUseCase createTodoUseCase(BoardRepository boardRepository) {
        return new CreateTodoUseCase(boardRepository);
    }
}
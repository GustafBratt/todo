package nu.forsenad.todo.infrastructure.config;

import nu.forsenad.todo.domain.IdGenerator;
import nu.forsenad.todo.domain.UuidIdGenerator;
import nu.forsenad.todo.ports.in.*;
import nu.forsenad.todo.ports.out.BoardRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    // IdGenerator bean - used by all create use cases
    @Bean
    public IdGenerator idGenerator() {
        return new UuidIdGenerator();
    }

    @Bean
    public CreateBoardUseCase createBoardUseCase(
            BoardRepository boardRepository,
            IdGenerator idGenerator) {
        return new CreateBoardUseCase(boardRepository, idGenerator);
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
    public CreateListUseCase createListUseCase(
            BoardRepository boardRepository,
            IdGenerator idGenerator) {
        return new CreateListUseCase(boardRepository, idGenerator);
    }

    @Bean
    public DeleteListUseCase deleteListUseCase(BoardRepository boardRepository) {
        return new DeleteListUseCase(boardRepository);
    }

    @Bean
    public UpdateListTitleUseCase updateListTitleUseCase(BoardRepository boardRepository) {
        return new UpdateListTitleUseCase(boardRepository);
    }

    @Bean
    public MoveListWithinBoardUseCase moveListWithinBoardUseCase(BoardRepository boardRepository) {
        return new MoveListWithinBoardUseCase(boardRepository);
    }

    @Bean
    public CreateTodoUseCase createTodoUseCase(
            BoardRepository boardRepository,
            IdGenerator idGenerator) {
        return new CreateTodoUseCase(boardRepository, idGenerator);
    }
}
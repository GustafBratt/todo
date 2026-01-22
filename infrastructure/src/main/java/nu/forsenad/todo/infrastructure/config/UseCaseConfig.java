package nu.forsenad.todo.infrastructure.config;

import nu.forsenad.todo.ports.in.CreateBoardUseCase;
import nu.forsenad.todo.ports.in.UpdateBoardUseCase;
import nu.forsenad.todo.ports.out.BoardRepository;
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
}
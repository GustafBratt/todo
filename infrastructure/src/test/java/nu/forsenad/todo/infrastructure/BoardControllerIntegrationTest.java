package nu.forsenad.todo.infrastructure;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import nu.forsenad.todo.infrastructure.outbound.entity.BoardEntity;
import nu.forsenad.todo.infrastructure.outbound.repo.SpringDataBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BoardControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    SpringDataBoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        boardRepository.deleteAll();
    }

    @Test
    void shouldCreateBoardAndPersistToDatabase() {
        // Given
        String boardName = "My Test Board";

        // When
        String boardId = given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"" + boardName + "\"}")
                .when()
                .post("/boards")
                .then()
                .statusCode(200)
                .body("name", equalTo(boardName))
                .body("id", notNullValue())
                .body("lists", empty())
                .extract()
                .path("id");

        // Then
        Optional<BoardEntity> savedBoard = boardRepository.findBoardWithListsById(boardId);

        assertThat(savedBoard).isPresent();
        assertThat(savedBoard.get().getName()).isEqualTo(boardName);
        assertThat(savedBoard.get().getLists()).isEmpty();
    }
}
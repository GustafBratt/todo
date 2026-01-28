package nu.forsenad.todo.infrastructure.inbound.rest.model;

// CreateListRequest.java
public class CreateListRequest {
    private String boardId;
    private String name;

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


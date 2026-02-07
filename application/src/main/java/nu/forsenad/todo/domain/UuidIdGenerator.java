package nu.forsenad.todo.domain;

import java.util.UUID;

public final class UuidIdGenerator implements IdGenerator {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
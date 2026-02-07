package nu.forsenad.todo.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Test implementation of IdGenerator that returns pre-configured IDs.
 * Useful for testing edge cases like duplicate IDs.
 */
public class FixedIdGenerator implements IdGenerator {
    private final List<String> ids;
    private int currentIndex = 0;

    public FixedIdGenerator(String... ids) {
        this.ids = new ArrayList<>(List.of(ids));
    }

    @Override
    public String generateId() {
        if (currentIndex >= ids.size()) {
            throw new IllegalStateException("No more IDs available");
        }
        return ids.get(currentIndex++);
    }

    /**
     * Create a generator that always returns the same ID (for testing duplicates)
     */
    public static FixedIdGenerator alwaysReturn(String id) {
        return new FixedIdGenerator(id, id, id, id, id, id, id, id, id, id);
    }
}
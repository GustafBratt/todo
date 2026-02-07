package nu.forsenad.todo.domain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test implementation of IdGenerator that produces predictable, sequential IDs.
 * Useful for testing scenarios where you need to control ID generation.
 */
public class SequentialIdGenerator implements IdGenerator {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final String prefix;

    public SequentialIdGenerator(String prefix) {
        this.prefix = prefix;
    }

    public SequentialIdGenerator() {
        this("id-");
    }

    @Override
    public String generateId() {
        return prefix + counter.incrementAndGet();
    }

    /**
     * Reset the counter for test isolation
     */
    public void reset() {
        counter.set(0);
    }
}
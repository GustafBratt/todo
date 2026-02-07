package nu.forsenad.todo.exception;

public class TodoDomainException extends RuntimeException {
    public TodoDomainException(String message) {
        super(message);
    }

    public TodoDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

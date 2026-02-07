package nu.forsenad.todo.exception;

public class BusinessRuleViolationException extends TodoDomainException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
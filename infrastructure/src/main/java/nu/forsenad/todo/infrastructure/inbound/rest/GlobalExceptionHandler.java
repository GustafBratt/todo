package nu.forsenad.todo.infrastructure.inbound.rest;

import nu.forsenad.todo.exception.BusinessRuleViolationException;
import nu.forsenad.todo.exception.ResourceNotFoundException;
import nu.forsenad.todo.exception.TodoDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
    public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setProperty("resourceType", ex.getResourceType());
        problemDetail.setProperty("resourceId", ex.getResourceId());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ProblemDetail> handleBusinessRule(BusinessRuleViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problemDetail.setTitle("Business Rule Violation");

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(TodoDomainException.class)
    public ResponseEntity<ProblemDetail> handleGeneric(TodoDomainException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
        problemDetail.setTitle("Internal Error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
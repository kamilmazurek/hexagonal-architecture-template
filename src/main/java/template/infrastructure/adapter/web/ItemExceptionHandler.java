package template.infrastructure.adapter.web;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import template.application.exception.InvalidItemException;
import template.application.exception.ItemAlreadyExistsException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestControllerAdvice
public class ItemExceptionHandler {

    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ProblemDetail handleItemAlreadyExists(ItemAlreadyExistsException exception) {
        return ProblemDetail.forStatusAndDetail(CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(InvalidItemException.class)
    public ProblemDetail handleInvalidItem(InvalidItemException exception) {
        return ProblemDetail.forStatusAndDetail(BAD_REQUEST, exception.getMessage());
    }

}

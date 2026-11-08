package template.application.exception;

public abstract class InvalidItemException extends RuntimeException {

    protected InvalidItemException(String message) {
        super(message);
    }

}

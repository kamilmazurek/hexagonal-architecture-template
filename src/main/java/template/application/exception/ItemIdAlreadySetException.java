package template.application.exception;

import static java.lang.String.format;

public final class ItemIdAlreadySetException extends RuntimeException {

    public static final String MESSAGE = "Item ID must be null when creating a new item. Expected null so the adapter can assign a new ID, but received: %s.";

    public ItemIdAlreadySetException(Long id) {
        super(format(MESSAGE, id));
    }

}
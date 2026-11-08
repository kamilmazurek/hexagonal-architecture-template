package template.application.exception;

import template.application.domain.model.ItemId;

import static java.lang.String.format;
import static template.application.domain.model.ItemId.MAX_LENGTH;

public final class InvalidItemNameException extends InvalidItemException {

    public static final String MESSAGE = "Item name must contain at least one letter or digit and produce an ID of at most %d characters, but received: %s.";

    public InvalidItemNameException(String name) {
        super(format(MESSAGE, MAX_LENGTH, name));
    }

}

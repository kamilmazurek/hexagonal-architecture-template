package template.application.exception;

import static java.lang.String.format;
import static template.application.domain.model.ItemId.MAX_LENGTH;

public final class InvalidItemIdException extends InvalidItemException {

    public static final String MESSAGE = "Item ID must consist of lowercase Latin letters and digits separated by single dashes and have at most %d characters, but received: %s.";

    public InvalidItemIdException(String id) {
        super(format(MESSAGE, MAX_LENGTH, id));
    }

}

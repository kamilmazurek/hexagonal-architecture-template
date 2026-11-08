package template.application.exception;

import template.application.domain.model.ItemId;

import static java.lang.String.format;

public final class ItemAlreadyExistsException extends RuntimeException {

    public static final String MESSAGE = "Item with id %s already exists";

    public ItemAlreadyExistsException(ItemId id) {
        super(format(MESSAGE, id));
    }

}

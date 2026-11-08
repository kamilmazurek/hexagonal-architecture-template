package template.application.exception;

import template.application.domain.model.ItemId;

import static java.lang.String.format;

public final class ItemIdMismatchException extends InvalidItemException {

    public static final String MESSAGE = "Item ID must be derived from the item name. Expected: %s, but received: %s.";

    public ItemIdMismatchException(ItemId expectedId, ItemId id) {
        super(format(MESSAGE, expectedId, id));
    }

}

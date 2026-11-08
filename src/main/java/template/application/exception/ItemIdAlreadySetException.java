package template.application.exception;

import template.application.domain.model.ItemId;

import static java.lang.String.format;

public final class ItemIdAlreadySetException extends InvalidItemException {

    public static final String MESSAGE = "Item ID must be null when creating a new item. Expected null so the ID can be derived from the item name, but received: %s.";

    public ItemIdAlreadySetException(ItemId id) {
        super(format(MESSAGE, id));
    }

}

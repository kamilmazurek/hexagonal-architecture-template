package template.application.exception;

import static java.lang.String.format;

public final class UnsupportedItemNameCharacterException extends InvalidItemException {

    public static final String MESSAGE = "Item name must use Latin letters and digits only, but received unsupported character: %s.";

    public UnsupportedItemNameCharacterException(int codePoint) {
        super(format(MESSAGE, Character.toString(codePoint)));
    }

}

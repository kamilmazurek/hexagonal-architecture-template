package template.application.domain.model;

import org.jspecify.annotations.NonNull;
import template.application.exception.InvalidItemIdException;

import java.util.regex.Pattern;

public record ItemId(String value) {

    public static final int MAX_LENGTH = 100;

    //words of lowercase Latin letters and digits joined by single dashes, e.g. "item-a-v2"
    private static final Pattern FORMAT = Pattern.compile("[a-z0-9]+(-[a-z0-9]+)*");

    public ItemId {
        if (!isValid(value)) {
            throw new InvalidItemIdException(value);
        }
    }

    public static boolean isValid(String value) {
        return value != null && value.length() <= MAX_LENGTH && FORMAT.matcher(value).matches();
    }

    @Override
    public @NonNull String toString() {
        return value;
    }

}

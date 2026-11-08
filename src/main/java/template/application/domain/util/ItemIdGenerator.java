package template.application.domain.util;

import lombok.experimental.UtilityClass;
import template.application.domain.model.ItemId;
import template.application.exception.InvalidItemNameException;
import template.application.exception.UnsupportedItemNameCharacterException;

import java.util.Map;

import static java.lang.Character.isLetterOrDigit;
import static java.util.Locale.ROOT;
import static java.util.Map.entry;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.stripAccents;
import static template.application.domain.model.ItemId.MAX_LENGTH;

@UtilityClass
public class ItemIdGenerator {

    private static final Map<String, String> SPECIAL_LETTERS = Map.ofEntries(
            entry("ß", "ss"),
            entry("ı", "i"),
            entry("þ", "th"),
            entry("ð", "d"),
            entry("ħ", "h"),
            entry("ə", "e"),
            entry("ŋ", "n"),
            entry("ŀ", "l"),
            entry("ø", "o"),
            entry("æ", "ae"),
            entry("œ", "oe")
    );

    public static ItemId fromName(String name) {
        if (name == null) {
            throw new InvalidItemNameException(null);
        }

        var words = toLatin(name).codePoints().mapToObj(ItemIdGenerator::toIdPart).collect(joining());
        var id = String.join("-", split(words));

        if (id.isEmpty() || id.length() > MAX_LENGTH) {
            throw new InvalidItemNameException(name);
        }

        return new ItemId(id);
    }

    private static String toLatin(String name) {
        var latin = name.toLowerCase(ROOT);

        for (var letter : SPECIAL_LETTERS.entrySet()) {
            latin = latin.replace(letter.getKey(), letter.getValue());
        }

        return stripAccents(latin);
    }

    private static String toIdPart(int codePoint) {
        if (isAsciiLetterOrDigit(codePoint)) {
            return Character.toString(codePoint);
        }

        if (isLetterOrDigit(codePoint)) {
            throw new UnsupportedItemNameCharacterException(codePoint);
        }

        return " ";
    }

    private static boolean isAsciiLetterOrDigit(int codePoint) {
        return (codePoint >= 'a' && codePoint <= 'z') || (codePoint >= '0' && codePoint <= '9');
    }

}

package template.application.domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import template.application.exception.InvalidItemIdException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static template.application.domain.model.ItemId.MAX_LENGTH;

class ItemIdTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "1", "item-a", "item-a-v2", "123-456"})
    void shouldCreateIdFromValidValue(String value) {
        //given valid value

        //when ID is created from value
        var id = new ItemId(value);

        //then ID holds the value
        assertEquals(value, id.value());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "Item-A", "item a", "item_a", "-item", "item-", "item--a", "żółw"})
    void shouldNotCreateIdFromInvalidValue(String value) {
        //given invalid value

        //when ID is created from value
        var exception = assertThrows(InvalidItemIdException.class, () -> new ItemId(value));

        //then exception with invalid value is thrown
        var expectedMessage = "Item ID must consist of lowercase Latin letters and digits separated by single dashes and have at most 100 characters, but received: " + value + ".";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {MAX_LENGTH + 1, MAX_LENGTH + 50})
    void shouldNotCreateIdLongerThanMaxLength(int length) {
        //given value longer than max length
        var value = "a".repeat(length);

        //when ID is created from value then exception is thrown
        assertThrows(InvalidItemIdException.class, () -> new ItemId(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"item-a", "item-a-v2"})
    void shouldUseValueAsStringRepresentation(String value) {
        //given ID
        var id = new ItemId(value);

        //when ID is converted to string
        var string = id.toString();

        //then value is returned
        assertEquals(value, string);
    }

}

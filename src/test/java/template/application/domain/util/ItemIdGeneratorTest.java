package template.application.domain.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import template.application.domain.model.ItemId;
import template.application.exception.InvalidItemNameException;
import template.application.exception.UnsupportedItemNameCharacterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static template.application.domain.model.ItemId.MAX_LENGTH;

class ItemIdGeneratorTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "Item A         | item-a",
            "item a         | item-a",
            "Item-A         | item-a",
            "'  Item   A!  '| item-a",
            "Item_A.v2      | item-a-v2",
            "Item 🚀 A      | item-a",
            "123            | 123",
            "Żółw           | zolw",
            "Crème Brûlée   | creme-brulee",
            "Crème          | creme",
            "Straße         | strasse",
            "Kılıç          | kilic",
            "Þórður         | thordur",
            "Ħamrun         | hamrun",
            "Əli            | eli",
            "Paraŀlel       | parallel",
            "Ĳsselmeer      | ijsselmeer",
            "ﬁle            | file",
            "Ａ１            | a1",
            "x²             | x2"
    })
    void shouldGenerateIdFromName(String name, String expectedId) {
        //given name

        //when ID is generated from name
        var id = ItemIdGenerator.fromName(name);

        //then expected ID is generated
        assertEquals(new ItemId(expectedId), id);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "!!!", "🚀"})
    void shouldNotGenerateIdFromNameWithoutLettersOrDigits(String name) {
        //given name without letters or digits

        //when ID is generated from name then exception is thrown
        assertThrows(InvalidItemNameException.class, () -> ItemIdGenerator.fromName(name));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "Item Ж  | ж",
            "日本     | 日",
            "Ελλάδα  | ε",
            "Item ٣  |  ٣"
    })
    void shouldNotGenerateIdFromNameWithUnsupportedCharacter(String name, String unsupportedCharacter) {
        //given name with unsupported character

        //when ID is generated from name
        var exception = assertThrows(UnsupportedItemNameCharacterException.class, () -> ItemIdGenerator.fromName(name));

        //then exception with unsupported character is thrown
        var expectedMessage = "Item name must use Latin letters and digits only, but received unsupported character: " + unsupportedCharacter + ".";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {MAX_LENGTH + 1, MAX_LENGTH + 50})
    void shouldNotGenerateIdLongerThanMaxLength(int length) {
        //given name that produces ID longer than max length
        var name = "a".repeat(length);

        //when ID is generated from name then exception is thrown
        assertThrows(InvalidItemNameException.class, () -> ItemIdGenerator.fromName(name));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, MAX_LENGTH})
    void shouldGenerateIdUpToMaxLength(int length) {
        //given name that produces ID up to max length
        var name = "a".repeat(length);

        //when ID is generated from name
        var id = ItemIdGenerator.fromName(name);

        //then ID is generated
        assertEquals(new ItemId(name), id);
    }

}

package ru.nsu.ineverovich.task122.contracts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.NEW_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.OLD_VALUE;

import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.HashTable;

/** Проверяет строковое представление без привязки к порядку обхода корзин. */
class StringRepresentationTest {
    @Test
    void formatsAnEmptyTable() {
        assertEquals("{}", new HashTable<>().toString());
    }

    @Test
    void formatsSingleMapping() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        assertEquals("{key=" + OLD_VALUE + "}", table.toString());
    }

    @Test
    void formatsNullsAndMultipleMappings() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(null, null);
        table.put("key", NEW_VALUE);
        String result = table.toString();
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
        assertTrue(result.contains("null=null"));
        assertTrue(result.contains("key=" + NEW_VALUE));
        assertTrue(result.contains(", "));
    }

    @Test
    void formatsDirectSelfReference() {
        HashTable<String, Object> table = new HashTable<>();
        table.put("self", table);
        assertEquals("{self=(this HashTable)}", table.toString());
    }
}

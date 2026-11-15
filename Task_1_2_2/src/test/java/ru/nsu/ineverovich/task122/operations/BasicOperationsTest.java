package ru.nsu.ineverovich.task122.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.EMPTY_COUNT;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.INITIAL_NUMBER;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.NEW_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.OLD_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.ONE_ENTRY;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.UPDATED_NUMBER;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.HashTable;

/** Проверяет операции по ключу и пример с типом Number из задания. */
class BasicOperationsTest {
    @Test
    void newTableIsEmpty() {
        HashTable<String, Integer> table = new HashTable<>();
        assertTrue(table.isEmpty());
        assertEquals(EMPTY_COUNT, table.size());
        assertFalse(table.containsKey("missing"));
        assertNull(table.get("missing"));
    }

    @Test
    void insertionAndReplacementKeepOneMapping() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.put("key", OLD_VALUE));
        assertFalse(table.isEmpty());
        assertTrue(table.containsKey(new String("key")));
        assertEquals(OLD_VALUE, table.get(new String("key")));
        assertEquals(OLD_VALUE, table.put("key", NEW_VALUE));
        assertEquals(NEW_VALUE, table.get("key"));
        assertEquals(ONE_ENTRY, table.size());
    }

    @Test
    void updateReplacesAnExistingValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        assertEquals(OLD_VALUE, table.update("key", NEW_VALUE));
        assertEquals(NEW_VALUE, table.get("key"));
        assertEquals(ONE_ENTRY, table.size());
    }

    @Test
    void updateRejectsAnAbsentKey() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(NoSuchElementException.class, () -> table.update("missing", NEW_VALUE));
        assertTrue(table.isEmpty());
    }

    @Test
    void supportsDifferentNumberSubtypes() {
        HashTable<String, Number> table = new HashTable<>();
        table.put("one", INITIAL_NUMBER);
        table.update("one", UPDATED_NUMBER);
        assertEquals(UPDATED_NUMBER, table.get("one"));
    }

    @Test
    void clearAllowsReuse() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("old", OLD_VALUE);
        table.clear();
        table.clear();
        assertTrue(table.isEmpty());
        assertNull(table.get("old"));
        table.put("new", NEW_VALUE);
        assertEquals(NEW_VALUE, table.get("new"));
        assertEquals(ONE_ENTRY, table.size());
    }
}

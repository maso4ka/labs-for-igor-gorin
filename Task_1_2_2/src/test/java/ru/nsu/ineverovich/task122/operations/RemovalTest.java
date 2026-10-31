package ru.nsu.ineverovich.task122.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.NEW_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.OLD_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.ONE_ENTRY;

import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.HashTable;

/** Проверяет удаление по ключу и условное удаление пары. */
class RemovalTest {
    @Test
    void removesAnExistingKeyExactlyOnce() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("first", OLD_VALUE);
        table.put("second", NEW_VALUE);
        assertEquals(OLD_VALUE, table.remove("first"));
        assertNull(table.remove("first"));
        assertFalse(table.containsKey("first"));
        assertEquals(NEW_VALUE, table.get("second"));
        assertEquals(ONE_ENTRY, table.size());
    }

    @Test
    void missingRemovalLeavesTheTableUnchanged() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.remove("missing"));
        table.put("key", OLD_VALUE);
        assertNull(table.remove("missing"));
        assertEquals(ONE_ENTRY, table.size());
        assertEquals(OLD_VALUE, table.get("key"));
    }

    @Test
    void conditionalRemovalRequiresTheCorrectValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        assertFalse(table.remove("key", NEW_VALUE));
        assertFalse(table.remove("absent", OLD_VALUE));
        assertEquals(OLD_VALUE, table.get("key"));
        assertTrue(table.remove("key", OLD_VALUE));
        assertFalse(table.containsKey("key"));
        assertTrue(table.isEmpty());
    }
}

package ru.nsu.ineverovich.task122.operations;

import static org.junit.jupiter.api.Assertions.*;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.*;

import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.HashTable;

/** Проверяет различие между отсутствующим ключом и сохранённым значением null. */
class NullHandlingTest {
    @Test
    void supportsNullKeyAndItsReplacement() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(null, OLD_VALUE);
        assertTrue(table.containsKey(null));
        assertEquals(OLD_VALUE, table.get(null));
        assertEquals(OLD_VALUE, table.put(null, NEW_VALUE));
        assertEquals(NEW_VALUE, table.update(null, OTHER_VALUE));
        assertEquals(OTHER_VALUE, table.remove(null));
        assertFalse(table.containsKey(null));
        assertTrue(table.isEmpty());
    }

    @Test
    void containsKeyFindsNullValues() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("nullable", null);
        assertNull(table.get("nullable"));
        assertTrue(table.containsKey("nullable"));
        assertFalse(table.containsKey("missing"));
        assertEquals(ONE_ENTRY, table.size());
    }

    @Test
    void updatesFromAndToNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", null);
        assertNull(table.update("key", OLD_VALUE));
        assertEquals(OLD_VALUE, table.update("key", null));
        assertTrue(table.containsKey("key"));
    }

    @Test
    void conditionalRemovalSupportsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(null, null);
        assertFalse(table.remove(null, OLD_VALUE));
        assertTrue(table.remove(null, null));
        assertFalse(table.remove(null, null));
        assertTrue(table.isEmpty());
    }

    @Test
    void removingNullValueStillDecreasesSize() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", null);
        assertNull(table.remove("key"));
        assertFalse(table.containsKey("key"));
        assertEquals(EMPTY_COUNT, table.size());
    }
}

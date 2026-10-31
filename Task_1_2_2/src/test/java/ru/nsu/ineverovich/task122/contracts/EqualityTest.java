package ru.nsu.ineverovich.task122.contracts;

import static org.junit.jupiter.api.Assertions.*;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.*;

import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.HashTable;

/** Проверяет контракты equals и hashCode для содержимого таблицы. */
class EqualityTest {
    @Test
    void equalityIgnoresInsertionOrderAndPreviousResizes() {
        HashTable<Integer, Integer> first = new HashTable<>();
        HashTable<Integer, Integer> second = new HashTable<>();
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            first.put(key, key);
        }
        for (int key = ONE_ENTRY; key < BULK_ENTRIES; key++) {
            first.remove(key);
        }
        first.put(null, null);
        second.put(null, null);
        second.put(EMPTY_COUNT, EMPTY_COUNT);
        assertEquals(first, second);
        assertEquals(second, first);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(first, first);
    }

    @Test
    void equalityRejectsDifferentValuesKeysSizesAndTypes() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        assertEquals(first, second);
        first.put("key", OLD_VALUE);
        assertNotEquals(first, second);
        second.put("key", NEW_VALUE);
        assertNotEquals(first, second);
        second.clear();
        second.put("other", OLD_VALUE);
        assertNotEquals(first, second);
        assertNotEquals(first, null);
        assertNotEquals(first, "{key=10}");
    }

    @Test
    void nullValueIsDifferentFromAnAbsentKey() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("first", null);
        second.put("second", null);
        assertNotEquals(first, second);
        second.clear();
        second.put("first", null);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}

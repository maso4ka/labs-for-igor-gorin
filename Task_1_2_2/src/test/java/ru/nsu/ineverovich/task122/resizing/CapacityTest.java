package ru.nsu.ineverovich.task122;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.EMPTY_COUNT;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.EXPANDED_BUCKETS;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.GROWTH_THRESHOLD_SIZE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.GROWTH_TRIGGER_SIZE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.MINIMUM_BUCKETS;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.NEW_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.OLD_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.SHRINK_THRESHOLD_SIZE;

import org.junit.jupiter.api.Test;

/** Проверяет изменение ёмкости через реальные операции с таблицей. */
class CapacityTest {
    @Test
    void growsOnlyAfterUpperThreshold() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int key = EMPTY_COUNT; key < GROWTH_THRESHOLD_SIZE; key++) {
            table.put(key, key);
        }
        assertEquals(MINIMUM_BUCKETS, table.capacity());
        table.put(GROWTH_THRESHOLD_SIZE, NEW_VALUE);
        assertEquals(EXPANDED_BUCKETS, table.capacity());
    }

    @Test
    void shrinksOnlyBelowLowerThreshold() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int key = EMPTY_COUNT; key < GROWTH_TRIGGER_SIZE; key++) {
            table.put(key, key);
        }
        for (int key = EMPTY_COUNT; table.size() > SHRINK_THRESHOLD_SIZE; key++) {
            table.remove(key);
        }
        assertEquals(EXPANDED_BUCKETS, table.capacity());
        table.remove(GROWTH_THRESHOLD_SIZE);
        assertEquals(MINIMUM_BUCKETS, table.capacity());
    }

    @Test
    void keepsMinimumCapacityWhenEmpty() {
        HashTable<Integer, Integer> table = new HashTable<>();
        assertEquals(MINIMUM_BUCKETS, table.capacity());
        table.put(OLD_VALUE, NEW_VALUE);
        table.remove(OLD_VALUE);
        assertEquals(MINIMUM_BUCKETS, table.capacity());
        table.clear();
        assertEquals(MINIMUM_BUCKETS, table.capacity());
    }
}

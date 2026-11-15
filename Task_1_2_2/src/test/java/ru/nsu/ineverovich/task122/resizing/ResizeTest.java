package ru.nsu.ineverovich.task122;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.BULK_ENTRIES;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.COLLISION_HASH;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.EMPTY_COUNT;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.MINIMUM_BUCKETS;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.NEW_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.OLD_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.ONE_ENTRY;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.fixtures.FixedHashKey;

/** Проверяет сохранение пар и освобождение избыточной памяти корзин. */
class ResizeTest {
    @Test
    void mappingsSurviveRepeatedGrowthAndShrink() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            table.put(key, key);
        }
        assertEquals(BULK_ENTRIES, table.size());
        Set<Integer> visited = new HashSet<>();
        for (Map.Entry<Integer, Integer> entry : table) {
            assertEquals(entry.getKey(), entry.getValue());
            assertTrue(visited.add(entry.getKey()));
        }
        assertEquals(BULK_ENTRIES, visited.size());
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            assertEquals(key, table.get(key));
            assertEquals(key, table.remove(key));
            assertFalse(table.containsKey(key));
        }
        assertTrue(table.isEmpty());
        table.put(OLD_VALUE, NEW_VALUE);
        assertEquals(NEW_VALUE, table.get(OLD_VALUE));
    }

    @Test
    void resizingPreservesLongCollisionChainsAndNullKey() {
        HashTable<FixedHashKey, Integer> table = new HashTable<>();
        table.put(null, null);
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            table.put(new FixedHashKey(String.valueOf(key), COLLISION_HASH), key);
        }
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            FixedHashKey lookup = new FixedHashKey(String.valueOf(key), COLLISION_HASH);
            assertEquals(key, table.get(lookup));
            assertEquals(key, table.remove(lookup));
        }
        assertTrue(table.containsKey(null));
        assertEquals(ONE_ENTRY, table.size());
        assertTrue(table.remove(null, null));
        assertTrue(table.isEmpty());
    }

    @Test
    void deletionAndClearReturnCapacityToTheMinimum() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            table.put(key, key);
        }
        assertTrue(table.capacity() > MINIMUM_BUCKETS);
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            table.remove(key);
        }
        assertEquals(MINIMUM_BUCKETS, table.capacity());
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            table.put(key, key);
        }
        table.clear();
        assertEquals(MINIMUM_BUCKETS, table.capacity());
        assertEquals(EMPTY_COUNT, table.size());
    }
}

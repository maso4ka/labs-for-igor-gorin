package ru.nsu.ineverovich.task122.resizing;

import static org.junit.jupiter.api.Assertions.*;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.HashTable;
import ru.nsu.ineverovich.task122.core.TableState;
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
        TableState<Integer, Integer> state = new TableState<>();
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            state.put(key, key);
        }
        assertTrue(state.buckets().capacity() > MINIMUM_BUCKETS);
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            state.remove(key);
        }
        assertEquals(MINIMUM_BUCKETS, state.buckets().capacity());
        for (int key = EMPTY_COUNT; key < BULK_ENTRIES; key++) {
            state.put(key, key);
        }
        state.clear();
        assertEquals(MINIMUM_BUCKETS, state.buckets().capacity());
        assertEquals(EMPTY_COUNT, state.size());
    }
}

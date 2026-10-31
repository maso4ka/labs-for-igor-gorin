package ru.nsu.ineverovich.task122.model;

import static org.junit.jupiter.api.Assertions.*;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.HashTable;

/** Сравнивает воспроизводимую последовательность операций с эталонной HashMap из JDK. */
class RandomizedModelTest {
    @Test
    void matchesHashMapAcrossMixedOperations() {
        Random random = new Random(RANDOM_SEED);
        HashTable<Integer, Integer> actual = new HashTable<>();
        Map<Integer, Integer> expected = new HashMap<>();
        for (int step = EMPTY_COUNT; step < RANDOM_STEPS; step++) {
            Integer key = random.nextInt(NULL_KEY_FREQUENCY) == EMPTY_COUNT
                    ? null : random.nextInt(RANDOM_KEY_BOUND);
            Integer value = random.nextBoolean() ? null : random.nextInt(RANDOM_VALUE_BOUND);
            applyOperation(random, expected, actual, key, value);
            assertContentsMatch(expected, actual);
        }
    }

    private void applyOperation(Random random, Map<Integer, Integer> expected,
                                HashTable<Integer, Integer> actual, Integer key, Integer value) {
        switch (random.nextInt(OPERATION_COUNT)) {
            case PUT_OPERATION:
                assertEquals(expected.put(key, value), actual.put(key, value));
                break;
            case REMOVE_OPERATION:
                assertEquals(expected.remove(key), actual.remove(key));
                break;
            case UPDATE_OPERATION:
                if (expected.containsKey(key)) {
                    assertEquals(expected.replace(key, value), actual.update(key, value));
                } else {
                    assertThrows(NoSuchElementException.class, () -> actual.update(key, value));
                }
                break;
            case REMOVE_PAIR_OPERATION:
                assertEquals(expected.remove(key, value), actual.remove(key, value));
                break;
            case CLEAR_OPERATION:
                if (random.nextInt(CLEAR_FREQUENCY) == EMPTY_COUNT) {
                    expected.clear();
                    actual.clear();
                }
                break;
            case READ_OPERATION:
                assertEquals(expected.get(key), actual.get(key));
                assertEquals(expected.containsKey(key), actual.containsKey(key));
                break;
            default:
                fail("Unknown test operation");
        }
    }

    private void assertContentsMatch(Map<Integer, Integer> expected,
                                     HashTable<Integer, Integer> actual) {
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
        Map<Integer, Integer> visited = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : actual) {
            assertFalse(visited.containsKey(entry.getKey()));
            visited.put(entry.getKey(), entry.getValue());
        }
        assertEquals(expected, visited);
        expected.forEach((key, value) -> {
            assertTrue(actual.containsKey(key));
            assertEquals(value, actual.get(key));
        });
    }
}

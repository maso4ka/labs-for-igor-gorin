package ru.nsu.ineverovich.task122.iteration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.COLLISION_HASH;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.NEW_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.OLD_VALUE;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.THREE_ENTRIES;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.HashTable;
import ru.nsu.ineverovich.task122.fixtures.FixedHashKey;

/** Проверяет обход, исчерпание итератора, null и неизменяемость снимков пар. */
class IterationTest {
    @Test
    void emptyIteratorIsExhausted() {
        Iterator<Map.Entry<String, Integer>> iterator = new HashTable<String, Integer>().iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void visitsAllMappingsOnceIncludingNulls() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("first", OLD_VALUE);
        table.put("second", null);
        table.put(null, NEW_VALUE);
        Map<String, Integer> actual = new HashMap<>();
        for (Map.Entry<String, Integer> entry : table) {
            assertFalse(actual.containsKey(entry.getKey()));
            actual.put(entry.getKey(), entry.getValue());
        }
        assertEquals(THREE_ENTRIES, actual.size());
        assertEquals(OLD_VALUE, actual.get("first"));
        assertTrue(actual.containsKey("second"));
        assertNull(actual.get("second"));
        assertEquals(NEW_VALUE, actual.get(null));
    }

    @Test
    void nextDoesNotRequireHasNextAndHasNextDoesNotAdvance() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(Map.entry("key", OLD_VALUE), iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("key", table.iterator().next().getKey());
    }

    @Test
    void entriesAreImmutableSnapshotsAndIteratorRemovalIsUnsupported() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        Map.Entry<String, Integer> entry = iterator.next();
        assertThrows(UnsupportedOperationException.class, () -> entry.setValue(NEW_VALUE));
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        table.update("key", NEW_VALUE);
        assertEquals(OLD_VALUE, entry.getValue());
    }

    @Test
    void traversesWholeCollisionChain() {
        HashTable<FixedHashKey, String> table = new HashTable<>();
        for (String id : new String[] {"alpha", "beta", "gamma"}) {
            table.put(new FixedHashKey(id, COLLISION_HASH), id);
        }
        Map<FixedHashKey, String> actual = new HashMap<>();
        table.forEach(entry -> actual.put(entry.getKey(), entry.getValue()));
        assertEquals(THREE_ENTRIES, actual.size());
        for (String id : new String[] {"alpha", "beta", "gamma"}) {
            assertEquals(id, actual.get(new FixedHashKey(id, COLLISION_HASH)));
        }
    }
}

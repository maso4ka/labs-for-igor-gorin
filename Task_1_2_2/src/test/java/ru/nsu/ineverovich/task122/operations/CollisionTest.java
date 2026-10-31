package ru.nsu.ineverovich.task122.operations;

import static org.junit.jupiter.api.Assertions.*;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.nsu.ineverovich.task122.HashTable;
import ru.nsu.ineverovich.task122.fixtures.FixedHashKey;

/** Проверяет коллизии хешей и корзин, а также крайние значения хеша. */
class CollisionTest {
    @ParameterizedTest
    @ValueSource(strings = {"alpha", "beta", "gamma"})
    void removesHeadMiddleOrTailWithoutLosingOtherNodes(String removed) {
        HashTable<FixedHashKey, String> table = new HashTable<>();
        for (String key : new String[] {"alpha", "beta", "gamma"}) {
            table.put(new FixedHashKey(key, COLLISION_HASH), key);
        }
        assertEquals(removed, table.remove(new FixedHashKey(removed, COLLISION_HASH)));
        for (String key : new String[] {"alpha", "beta", "gamma"}) {
            String expected = key.equals(removed) ? null : key;
            assertEquals(expected, table.get(new FixedHashKey(key, COLLISION_HASH)));
        }
        assertEquals(TWO_ENTRIES, table.size());
        assertNull(table.remove(new FixedHashKey("absent", COLLISION_HASH)));
    }

    @Test
    void updatesAndReplacesInsideACollisionChain() {
        HashTable<FixedHashKey, Integer> table = new HashTable<>();
        table.put(new FixedHashKey("first", COLLISION_HASH), OLD_VALUE);
        table.put(new FixedHashKey("second", COLLISION_HASH), NEW_VALUE);
        assertEquals(OLD_VALUE, table.update(new FixedHashKey("first", COLLISION_HASH), OTHER_VALUE));
        assertEquals(OTHER_VALUE, table.put(new FixedHashKey("first", COLLISION_HASH), NEW_VALUE));
        assertEquals(TWO_ENTRIES, table.size());
        assertEquals(NEW_VALUE, table.get(new FixedHashKey("second", COLLISION_HASH)));
    }

    @Test
    void distinguishesDifferentHashesThatShareABucket() {
        HashTable<FixedHashKey, Integer> table = new HashTable<>();
        FixedHashKey first = new FixedHashKey("first", COLLISION_HASH);
        FixedHashKey second = new FixedHashKey("second", SAME_BUCKET_HASH);
        table.put(first, OLD_VALUE);
        table.put(second, NEW_VALUE);
        assertEquals(OLD_VALUE, table.get(first));
        assertEquals(OLD_VALUE, table.remove(first));
        assertEquals(NEW_VALUE, table.get(second));
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, Integer.MAX_VALUE, NEGATIVE_HASH, ZERO_HASH, HIGH_BITS_HASH})
    void acceptsEverySignOfHash(int hash) {
        HashTable<FixedHashKey, Integer> table = new HashTable<>();
        table.put(new FixedHashKey("key", hash), OLD_VALUE);
        assertEquals(OLD_VALUE, table.get(new FixedHashKey("key", hash)));
        assertEquals(OLD_VALUE, table.remove(new FixedHashKey("key", hash)));
        assertTrue(table.isEmpty());
    }
}

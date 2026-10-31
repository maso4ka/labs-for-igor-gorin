package ru.nsu.ineverovich.task122.iteration;

import static org.junit.jupiter.api.Assertions.*;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.nsu.ineverovich.task122.HashTable;

/** Проверяет структурные изменения, замены значений и операции без изменений. */
class FailFastTest {
    @ParameterizedTest
    @ValueSource(strings = {"insert", "remove", "removePair", "clear"})
    void structuralChangesInvalidateHasNextAndNext(String operation) {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        switch (operation) {
            case "insert": table.put("new", NEW_VALUE); break;
            case "remove": table.remove("key"); break;
            case "removePair": table.remove("key", OLD_VALUE); break;
            case "clear": table.clear(); break;
            default: fail("Unknown test operation");
        }
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void replacementsKeepAnIteratorValidAndExposeTheNewValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        table.put("key", NEW_VALUE);
        table.update("key", OTHER_VALUE);
        assertTrue(iterator.hasNext());
        assertEquals(OTHER_VALUE, iterator.next().getValue());
    }

    @Test
    void unsuccessfulRemovalDoesNotInvalidateAnIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        table.remove("absent");
        table.remove("key", NEW_VALUE);
        assertEquals(OLD_VALUE, iterator.next().getValue());
    }

    @Test
    void insertionThenRemovalStillInvalidatesAnEmptyIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        table.put("temporary", OLD_VALUE);
        table.remove("temporary");
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
    }

    @Test
    void clearingAnEmptyTableIsNotAStructuralChange() {
        HashTable<String, Integer> table = new HashTable<>();
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        table.clear();
        assertFalse(iterator.hasNext());
    }

    @Test
    void modificationInsideForEachIsDetected() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", OLD_VALUE);
        assertThrows(ConcurrentModificationException.class,
                () -> table.forEach(entry -> table.put("new", NEW_VALUE)));
    }
}

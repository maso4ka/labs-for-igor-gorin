package ru.nsu.ineverovich.task122.iteration;

import static ru.nsu.ineverovich.task122.config.HashTableConstants.FIRST_BUCKET_INDEX;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import ru.nsu.ineverovich.task122.core.TableState;
import ru.nsu.ineverovich.task122.storage.Node;

/**
 * Итератор проверяет структурные изменения и возвращает неизменяемые снимки пар.
 * Удаление через итератор не поддерживается; замена значения не меняет структуру.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public final class HashTableIterator<K, V> implements Iterator<Map.Entry<K, V>> {
    private final TableState<K, V> state;
    private final int expectedModifications;
    private int bucketIndex = FIRST_BUCKET_INDEX;
    private Node<K, V> next;

    /** Создаёт итератор для текущего содержимого таблицы. */
    public HashTableIterator(TableState<K, V> state) {
        this.state = state;
        expectedModifications = state.modifications();
        advanceToBucket();
    }

    @Override
    public boolean hasNext() {
        checkForModification();
        return next != null;
    }

    @Override
    public Map.Entry<K, V> next() {
        checkForModification();
        if (next == null) {
            throw new NoSuchElementException("No more hash table entries");
        }
        Map.Entry<K, V> entry = new SimpleImmutableEntry<>(next.key(), next.value());
        next = next.next();
        advanceToBucket();
        return entry;
    }

    private void advanceToBucket() {
        while (next == null && bucketIndex < state.buckets().capacity()) {
            next = state.buckets().head(bucketIndex);
            bucketIndex++;
        }
    }

    private void checkForModification() {
        if (expectedModifications != state.modifications()) {
            throw new ConcurrentModificationException("Hash table structure changed");
        }
    }
}

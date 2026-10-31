package ru.nsu.ineverovich.task122.core;

import static ru.nsu.ineverovich.task122.config.HashTableConstants.EMPTY_SIZE;
import static ru.nsu.ineverovich.task122.config.HashTableConstants.INITIAL_MODIFICATION_COUNT;

import ru.nsu.ineverovich.task122.storage.BucketArray;
import ru.nsu.ineverovich.task122.storage.Node;

/**
 * Внутреннее изменяемое состояние и счётчик структурных изменений.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public final class TableState<K, V> {
    private BucketArray<K, V> buckets = new BucketArray<>();
    private int size = EMPTY_SIZE;
    private int modifications = INITIAL_MODIFICATION_COUNT;

    /** Возвращает количество сохранённых пар. */
    public int size() {
        return size;
    }

    /** Возвращает счётчик для обнаружения структурных изменений при обходе. */
    public int modifications() {
        return modifications;
    }

    /** Возвращает внутренний массив корзин. */
    public BucketArray<K, V> buckets() {
        return buckets;
    }

    /** Возвращает узел по ключу либо null. */
    public Node<K, V> find(Object key) {
        return buckets.find(key);
    }

    /** Добавляет или заменяет значение и возвращает прежнее значение либо null. */
    public V put(K key, V value) {
        Node<K, V> existing = find(key);
        if (existing != null) {
            return existing.replaceValue(value);
        }
        buckets.insert(key, value);
        size++;
        modifications++;
        rebalance();
        return null;
    }

    /** Удаляет ключ и возвращает прежнее значение либо null. */
    public V remove(Object key) {
        Node<K, V> removed = buckets.unlink(key);
        if (removed == null) {
            return null;
        }
        size--;
        modifications++;
        rebalance();
        return removed.value();
    }

    /** Очищает содержимое и освобождает избыточную память массива корзин. */
    public void clear() {
        if (size == EMPTY_SIZE) {
            return;
        }
        buckets = new BucketArray<>();
        size = EMPTY_SIZE;
        modifications++;
    }

    private void rebalance() {
        buckets.resize(CapacityPolicy.capacityFor(size, buckets.capacity()));
    }
}

package ru.nsu.ineverovich.task122;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Хеш-таблица с цепочками коллизий и ожидаемыми амортизированными операциями O(1).
 * Ключи и значения могут быть null; результаты equals/hashCode ключей должны быть стабильны.
 * Класс не потокобезопасен, порядок обхода не определён.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public final class HashTable<K, V> implements Iterable<Entry<K, V>> {
    private static final int MINIMUM_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
    private static final int MAXIMUM_CAPACITY = 1_073_741_824;
    private static final double MAXIMUM_LOAD_FACTOR = 0.75;
    private static final double MINIMUM_LOAD_FACTOR = 0.25;
    private static final int HASH_SPREAD_SHIFT = 16;
    private static final int INDEX_MASK_OFFSET = 1;
    private static final int EMPTY_SIZE = 0;
    private static final int EMPTY_HASH_CODE = 0;

    private List<Node<K, V>> buckets = emptyBuckets(MINIMUM_CAPACITY);
    private int size;
    private int modifications;

    /**
     * Добавляет или заменяет пару и возвращает прежнее значение либо null.
     *
     * @param key ключ пары
     * @param value сохраняемое значение
     * @return прежнее значение либо null
     */
    public V put(K key, V value) {
        Node<K, V> existing = find(key);
        if (existing != null) {
            return existing.replaceValue(value);
        }
        int index = index(key);
        buckets.set(index, new Node<>(key, value, buckets.get(index)));
        size++;
        modifications++;
        rebalance();
        return null;
    }

    /**
     * Возвращает значение по ключу либо null, если ключ отсутствует.
     *
     * @param key искомый ключ
     * @return сохранённое значение либо null
     */
    public V get(Object key) {
        Node<K, V> node = find(key);
        return node == null ? null : node.value;
    }

    /**
     * Обновляет значение; при отсутствии ключа выбрасывает NoSuchElementException.
     *
     * @param key ключ существующей пары
     * @param value новое значение
     * @return прежнее значение
     * @throws NoSuchElementException если ключ отсутствует
     */
    public V update(K key, V value) {
        Node<K, V> node = find(key);
        if (node == null) {
            throw new NoSuchElementException("Ключ отсутствует: " + key);
        }
        return node.replaceValue(value);
    }

    /**
     * Удаляет ключ и возвращает прежнее значение либо null при отсутствии ключа.
     *
     * @param key удаляемый ключ
     * @return удалённое значение либо null
     */
    public V remove(Object key) {
        int index = index(key);
        Node<K, V> previous = null;
        for (Node<K, V> node = buckets.get(index); node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                if (previous == null) {
                    buckets.set(index, node.next);
                } else {
                    previous.next = node.next;
                }
                size--;
                modifications++;
                rebalance();
                return node.value;
            }
            previous = node;
        }
        return null;
    }

    /**
     * Удаляет пару только при совпадении ключа и значения.
     *
     * @param key удаляемый ключ
     * @param value ожидаемое значение
     * @return true, если пара найдена и удалена
     */
    public boolean remove(Object key, Object value) {
        Node<K, V> node = find(key);
        if (node == null || !Objects.equals(node.value, value)) {
            return false;
        }
        remove(key);
        return true;
    }

    /**
     * Проверяет наличие ключа, в том числе со значением null.
     *
     * @param key проверяемый ключ
     * @return true, если ключ присутствует
     */
    public boolean containsKey(Object key) {
        return find(key) != null;
    }

    /**
     * Возвращает количество пар.
     *
     * @return количество сохранённых пар
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли таблица.
     *
     * @return true, если таблица пуста
     */
    public boolean isEmpty() {
        return size == EMPTY_SIZE;
    }

    /** Удаляет все пары и восстанавливает минимальную ёмкость. */
    public void clear() {
        if (isEmpty()) {
            return;
        }
        buckets = emptyBuckets(MINIMUM_CAPACITY);
        size = EMPTY_SIZE;
        modifications++;
    }

    /** Возвращает итератор по неизменяемым снимкам пар. */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new EntryIterator();
    }

    /** Сравнивает содержимое независимо от ёмкости и порядка добавления. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HashTable<?, ?>)) {
            return false;
        }
        HashTable<?, ?> that = (HashTable<?, ?>) other;
        if (size != that.size) {
            return false;
        }
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                Node<?, ?> match = that.find(node.key);
                if (match == null || !Objects.equals(node.value, match.value)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Вычисляет хеш содержимого, не зависящий от порядка пар. */
    @Override
    public int hashCode() {
        int result = EMPTY_HASH_CODE;
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                result += Objects.hashCode(node.key) ^ Objects.hashCode(node.value);
            }
        }
        return result;
    }

    /** Выводит пары в строку с обработкой прямых ссылок на саму таблицу. */
    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(", ", "{", "}");
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                result.add(formatPart(node.key) + "=" + formatPart(node.value));
            }
        }
        return result.toString();
    }

    private String formatPart(Object value) {
        return value == this ? "(this HashTable)" : String.valueOf(value);
    }

    private Node<K, V> find(Object key) {
        for (Node<K, V> node = buckets.get(index(key)); node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }

    private int index(Object key) {
        int hash = Objects.hashCode(key);
        return (hash ^ (hash >>> HASH_SPREAD_SHIFT)) & (buckets.size() - INDEX_MASK_OFFSET);
    }

    private void rebalance() {
        int capacity = buckets.size();
        if (size > capacity * MAXIMUM_LOAD_FACTOR && capacity < MAXIMUM_CAPACITY) {
            resize(capacity * CAPACITY_MULTIPLIER);
        } else if (size < capacity * MINIMUM_LOAD_FACTOR && capacity > MINIMUM_CAPACITY) {
            resize(capacity / CAPACITY_MULTIPLIER);
        }
    }

    private void resize(int capacity) {
        List<Node<K, V>> previous = buckets;
        buckets = emptyBuckets(capacity);
        for (Node<K, V> node : previous) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = index(node.key);
                node.next = buckets.get(index);
                buckets.set(index, node);
                node = next;
            }
        }
    }

    private List<Node<K, V>> emptyBuckets(int capacity) {
        return new ArrayList<>(Collections.nCopies(capacity, null));
    }

    int capacity() {
        return buckets.size();
    }

    /** Узел цепочки принадлежит только этой реализации таблицы. */
    private static final class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private V replaceValue(V replacement) {
            V previous = value;
            value = replacement;
            return previous;
        }
    }

    /** Обходит цепочки и проверяет структурные изменения перед каждой операцией. */
    private final class EntryIterator implements Iterator<Entry<K, V>> {
        private final int expectedModifications = modifications;
        private int bucketIndex;
        private Node<K, V> next;

        private EntryIterator() {
            advanceToBucket();
        }

        @Override
        public boolean hasNext() {
            checkForModification();
            return next != null;
        }

        @Override
        public Entry<K, V> next() {
            checkForModification();
            if (next == null) {
                throw new NoSuchElementException("Больше нет пар для обхода");
            }
            Entry<K, V> entry = new SimpleImmutableEntry<>(next.key, next.value);
            next = next.next;
            advanceToBucket();
            return entry;
        }

        private void advanceToBucket() {
            while (next == null && bucketIndex < buckets.size()) {
                next = buckets.get(bucketIndex);
                bucketIndex++;
            }
        }

        private void checkForModification() {
            if (expectedModifications != modifications) {
                throw new ConcurrentModificationException("Структура таблицы изменена");
            }
        }
    }
}

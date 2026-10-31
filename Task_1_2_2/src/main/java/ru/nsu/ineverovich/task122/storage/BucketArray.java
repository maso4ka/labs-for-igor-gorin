package ru.nsu.ineverovich.task122.storage;

import static ru.nsu.ineverovich.task122.config.HashTableConstants.MINIMUM_CAPACITY;

import java.util.Objects;

/**
 * Массив цепочек коллизий без дополнительных объектов для пустых корзин.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public final class BucketArray<K, V> {
    private Node<K, V>[] heads = createArray(MINIMUM_CAPACITY);

    /** Возвращает количество корзин. */
    public int capacity() {
        return heads.length;
    }

    /** Возвращает первый узел корзины по её внутреннему индексу. */
    public Node<K, V> head(int index) {
        return heads[index];
    }

    /** Ищет ключ по хешу и методу equals; при отсутствии возвращает null. */
    public Node<K, V> find(Object key) {
        int hash = Hashing.spread(key);
        for (Node<K, V> node = heads[index(hash)]; node != null; node = node.next()) {
            if (node.hash() == hash && Objects.equals(node.key(), key)) {
                return node;
            }
        }
        return null;
    }

    /** Добавляет ключ в начало цепочки; отсутствие ключа проверяет вызывающий код. */
    public void insert(K key, V value) {
        int hash = Hashing.spread(key);
        int index = index(hash);
        heads[index] = new Node<>(key, value, hash, heads[index]);
    }

    /** Исключает найденный узел из цепочки; при отсутствии ключа возвращает null. */
    public Node<K, V> unlink(Object key) {
        int hash = Hashing.spread(key);
        int index = index(hash);
        Node<K, V> previous = null;
        for (Node<K, V> node = heads[index]; node != null; node = node.next()) {
            if (node.hash() == hash && Objects.equals(node.key(), key)) {
                if (previous == null) {
                    heads[index] = node.next();
                } else {
                    previous.linkTo(node.next());
                }
                return node;
            }
            previous = node;
        }
        return null;
    }

    /** Переносит существующие узлы в новый массив без создания их копий. */
    public void resize(int capacity) {
        if (capacity == heads.length) {
            return;
        }
        Node<K, V>[] previous = heads;
        heads = createArray(capacity);
        for (Node<K, V> head : previous) {
            Node<K, V> node = head;
            while (node != null) {
                Node<K, V> next = node.next();
                int index = index(node.hash());
                node.linkTo(heads[index]);
                heads[index] = node;
                node = next;
            }
        }
    }

    private int index(int hash) {
        return Hashing.index(hash, capacity());
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Node<K, V>[] createArray(int capacity) {
        return (Node<K, V>[]) new Node<?, ?>[capacity];
    }
}

package ru.nsu.ineverovich.task122.storage;

/**
 * Узел цепочки; сохранённый хеш не нужно вычислять заново при изменении ёмкости.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public final class Node<K, V> {
    private final K key;
    private final int hash;
    private V value;
    private Node<K, V> next;

    /** Создаёт узел перед переданной цепочкой. */
    public Node(K key, V value, int hash, Node<K, V> next) {
        this.key = key;
        this.value = value;
        this.hash = hash;
        this.next = next;
    }

    /** Возвращает сохранённый ключ. */
    public K key() {
        return key;
    }

    /** Возвращает текущее значение. */
    public V value() {
        return value;
    }

    /** Возвращает сохранённый перемешанный хеш. */
    public int hash() {
        return hash;
    }

    /** Возвращает следующий узел цепочки либо null. */
    public Node<K, V> next() {
        return next;
    }

    /** Заменяет значение и возвращает прежнее значение. */
    public V replaceValue(V replacement) {
        V previous = value;
        value = replacement;
        return previous;
    }

    /** Меняет связь узлов при удалении или изменении ёмкости. */
    public void linkTo(Node<K, V> successor) {
        next = successor;
    }
}

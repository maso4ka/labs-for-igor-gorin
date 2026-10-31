package ru.nsu.ineverovich.task122;

import static ru.nsu.ineverovich.task122.config.HashTableConstants.EMPTY_SIZE;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import ru.nsu.ineverovich.task122.core.TableState;
import ru.nsu.ineverovich.task122.iteration.HashTableIterator;
import ru.nsu.ineverovich.task122.storage.Node;
import ru.nsu.ineverovich.task122.support.TableContracts;

/**
 * Хеш-таблица с цепочками коллизий и ожидаемыми амортизированными операциями O(1).
 * Ключи и значения могут быть null. Методы equals/hashCode ключей не должны меняться.
 * Класс не потокобезопасен; порядок обхода не определён.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public final class HashTable<K, V> implements Iterable<Map.Entry<K, V>> {
    private final TableState<K, V> state = new TableState<>();

    /** Добавляет или заменяет пару; возвращает прежнее значение либо null. */
    public V put(K key, V value) {
        return state.put(key, value);
    }

    /** Возвращает значение по ключу либо null, если ключ отсутствует. */
    public V get(Object key) {
        Node<K, V> node = state.find(key);
        return node == null ? null : node.value();
    }

    /** Обновляет значение или выбрасывает NoSuchElementException при отсутствии ключа. */
    public V update(K key, V value) {
        Node<K, V> node = state.find(key);
        if (node == null) {
            throw new NoSuchElementException("Key is absent: " + key);
        }
        return node.replaceValue(value);
    }

    /** Удаляет ключ; возвращает прежнее значение либо null, если ключ отсутствует. */
    public V remove(Object key) {
        return state.remove(key);
    }

    /** Удаляет пару только при совпадении ключа и значения. */
    public boolean remove(Object key, Object value) {
        Node<K, V> node = state.find(key);
        if (node == null || !Objects.equals(node.value(), value)) {
            return false;
        }
        state.remove(key);
        return true;
    }

    /** Проверяет наличие ключа, в том числе со значением null. */
    public boolean containsKey(Object key) {
        return state.find(key) != null;
    }

    /** Возвращает количество пар. */
    public int size() {
        return state.size();
    }

    /** Проверяет, пуста ли таблица. */
    public boolean isEmpty() {
        return size() == EMPTY_SIZE;
    }

    /** Удаляет все пары и восстанавливает минимальную ёмкость. */
    public void clear() {
        state.clear();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashTableIterator<>(state);
    }

    @Override
    public boolean equals(Object other) {
        return TableContracts.equal(this, other);
    }

    @Override
    public int hashCode() {
        return TableContracts.hash(this);
    }

    @Override
    public String toString() {
        return TableContracts.format(this);
    }
}

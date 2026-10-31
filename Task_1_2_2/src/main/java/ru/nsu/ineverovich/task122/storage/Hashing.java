package ru.nsu.ineverovich.task122.storage;

import static ru.nsu.ineverovich.task122.config.HashTableConstants.HASH_SPREAD_SHIFT;
import static ru.nsu.ineverovich.task122.config.HashTableConstants.INDEX_MASK_OFFSET;

import java.util.Objects;

/** Перемешивание хеша и выбор корзины в массиве с ёмкостью, равной степени двойки. */
public final class Hashing {
    private Hashing() {
    }

    /** Смешивает старшие биты хеша с младшими; поддерживает ключ null. */
    public static int spread(Object key) {
        int hash = Objects.hashCode(key);
        return hash ^ (hash >>> HASH_SPREAD_SHIFT);
    }

    /** Возвращает неотрицательный индекс, в том числе для хеша Integer.MIN_VALUE. */
    public static int index(int hash, int capacity) {
        return hash & (capacity - INDEX_MASK_OFFSET);
    }
}

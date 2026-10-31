package ru.nsu.ineverovich.task122.core;

import static ru.nsu.ineverovich.task122.config.HashTableConstants.CAPACITY_MULTIPLIER;
import static ru.nsu.ineverovich.task122.config.HashTableConstants.MAXIMUM_CAPACITY;
import static ru.nsu.ineverovich.task122.config.HashTableConstants.MAXIMUM_LOAD_FACTOR;
import static ru.nsu.ineverovich.task122.config.HashTableConstants.MINIMUM_CAPACITY;
import static ru.nsu.ineverovich.task122.config.HashTableConstants.MINIMUM_LOAD_FACTOR;

/** Разные пороги роста и сжатия предотвращают частое изменение ёмкости. */
public final class CapacityPolicy {
    private CapacityPolicy() {
    }

    /** Выбирает ёмкость, равную степени двойки, для текущего количества пар. */
    public static int capacityFor(int size, int capacity) {
        if (size > capacity * MAXIMUM_LOAD_FACTOR && capacity < MAXIMUM_CAPACITY) {
            return capacity * CAPACITY_MULTIPLIER;
        }
        if (size < capacity * MINIMUM_LOAD_FACTOR && capacity > MINIMUM_CAPACITY) {
            return capacity / CAPACITY_MULTIPLIER;
        }
        return capacity;
    }
}

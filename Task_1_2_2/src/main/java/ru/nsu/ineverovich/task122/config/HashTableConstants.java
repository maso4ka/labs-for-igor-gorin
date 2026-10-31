package ru.nsu.ineverovich.task122.config;

/** Именованные ограничения и начальные значения хеш-таблицы. */
public final class HashTableConstants {
    public static final int MINIMUM_CAPACITY = 16;
    public static final int CAPACITY_MULTIPLIER = 2;
    public static final int MAXIMUM_CAPACITY = 1_073_741_824;
    public static final double MAXIMUM_LOAD_FACTOR = 0.75;
    public static final double MINIMUM_LOAD_FACTOR = 0.25;
    public static final int HASH_SPREAD_SHIFT = 16;
    public static final int INDEX_MASK_OFFSET = 1;
    public static final int EMPTY_SIZE = 0;
    public static final int INITIAL_MODIFICATION_COUNT = 0;
    public static final int FIRST_BUCKET_INDEX = 0;
    public static final int EMPTY_HASH_CODE = 0;

    private HashTableConstants() {
    }
}

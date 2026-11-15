package ru.nsu.ineverovich.task122.fixtures;

/** Именованные входные данные, размеры и ограничения для тестов. */
public final class TestConstants {
    public static final int EMPTY_COUNT = 0;
    public static final int ONE_ENTRY = 1;
    public static final int TWO_ENTRIES = 2;
    public static final int THREE_ENTRIES = 3;
    public static final int OLD_VALUE = 10;
    public static final int NEW_VALUE = 20;
    public static final int OTHER_VALUE = 30;
    public static final double UPDATED_NUMBER = 1.0;
    public static final int INITIAL_NUMBER = 1;
    public static final int COLLISION_HASH = 7;
    public static final int SAME_BUCKET_HASH = 23;
    public static final int NEGATIVE_HASH = -37;
    public static final int ZERO_HASH = 0;
    public static final int HIGH_BITS_HASH = 65_536;
    public static final int MINIMUM_BUCKETS = 16;
    public static final int EXPANDED_BUCKETS = 32;
    public static final int GROWTH_THRESHOLD_SIZE = 12;
    public static final int GROWTH_TRIGGER_SIZE = 13;
    public static final int SHRINK_THRESHOLD_SIZE = 8;
    public static final int SHRINK_TRIGGER_SIZE = 7;
    public static final int BULK_ENTRIES = 2_000;
    public static final int RANDOM_STEPS = 20_000;
    public static final int RANDOM_KEY_BOUND = 128;
    public static final int RANDOM_VALUE_BOUND = 1_000;
    public static final int NULL_KEY_FREQUENCY = 10;
    public static final long RANDOM_SEED = 20_261_028L;
    public static final int PUT_OPERATION = 0;
    public static final int REMOVE_OPERATION = 1;
    public static final int UPDATE_OPERATION = 2;
    public static final int REMOVE_PAIR_OPERATION = 3;
    public static final int CLEAR_OPERATION = 4;
    public static final int READ_OPERATION = 5;
    public static final int OPERATION_COUNT = 6;
    public static final int CLEAR_FREQUENCY = 100;

    private TestConstants() {
    }
}

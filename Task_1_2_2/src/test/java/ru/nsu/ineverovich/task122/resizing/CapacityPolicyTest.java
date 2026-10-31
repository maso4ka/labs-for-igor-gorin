package ru.nsu.ineverovich.task122.resizing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.nsu.ineverovich.task122.config.HashTableConstants.MAXIMUM_CAPACITY;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.*;

import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task122.core.CapacityPolicy;

/** Проверяет пороги изменения ёмкости и защиту от переполнения без больших массивов. */
class CapacityPolicyTest {
    @Test
    void growsOnlyAfterTheUpperThreshold() {
        assertEquals(MINIMUM_BUCKETS,
                CapacityPolicy.capacityFor(GROWTH_THRESHOLD_SIZE, MINIMUM_BUCKETS));
        assertEquals(EXPANDED_BUCKETS,
                CapacityPolicy.capacityFor(GROWTH_TRIGGER_SIZE, MINIMUM_BUCKETS));
    }

    @Test
    void shrinksOnlyBelowTheLowerThreshold() {
        assertEquals(EXPANDED_BUCKETS,
                CapacityPolicy.capacityFor(SHRINK_THRESHOLD_SIZE, EXPANDED_BUCKETS));
        assertEquals(MINIMUM_BUCKETS,
                CapacityPolicy.capacityFor(SHRINK_TRIGGER_SIZE, EXPANDED_BUCKETS));
    }

    @Test
    void respectsMinimumAndMaximumArrayCapacity() {
        assertEquals(MINIMUM_BUCKETS,
                CapacityPolicy.capacityFor(EMPTY_COUNT, MINIMUM_BUCKETS));
        assertEquals(MAXIMUM_CAPACITY,
                CapacityPolicy.capacityFor(MAXIMUM_CAPACITY, MAXIMUM_CAPACITY));
    }
}

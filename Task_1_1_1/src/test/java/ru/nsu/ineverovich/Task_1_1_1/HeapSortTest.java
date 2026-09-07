package ru.nsu.ineverovich.Task_1_1_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {

    @Test
    void sort() {
        int[] array = new int[]{1, 3, 2};
        var result = HeapSort.sort(array);
        assertArrayEquals(new int[]{1, 3, 2}, result);
    }
}
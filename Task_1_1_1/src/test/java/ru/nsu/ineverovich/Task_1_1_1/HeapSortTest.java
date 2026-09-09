package ru.nsu.ineverovich.Task_1_1_1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {

    @Test
    public void testExample() {
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSorted() {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testReverse() {
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testDuplicates() {
        int[] input = {5, 3, 6, 7, 9, 6, 5, 5, 7};
        int[] expected = {3, 5, 5, 5, 6, 6, 7, 7, 9};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testAllEqual() {
        int[] input = {7, 7, 7, 7, 7, 7};
        int[] expected = {7, 7, 7, 7, 7, 7};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSingle() {
        int[] input = {67};
        int[] expected = {67};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testEmpty() {
        int[] input = {};
        int[] expected = {};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testOnlyTwo() {
        int[] input = {7, 6};
        int[] expected = {6, 7};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testMinus() {
        int[] input = {-5, -2, -8, -1, -3};
        int[] expected = {-8, -5, -3, -2, -1};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testMixed() {
        int[] input = {-5, 10, 0, -3, 7, -1};
        int[] expected = {-5, -3, -1, 0, 7, 10};
        HeapSort.sort(input);
        assertArrayEquals(expected, input);
    }
}
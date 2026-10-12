package ru.nsu.ineverovich.task113.model;

import java.util.List;

/**
 * Константа целого типа в выражении.
 */
public final class Number extends Expression {
    private final int value;

    /**
     * Создаёт константу.
     *
     * @param value значение константы.
     */
    public Number(int value) {
        this.value = value;
    }

    @Override
    public void print() {
        System.out.println(value);
    }

    @Override
    public int eval(List<String> values) {
        return value;
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

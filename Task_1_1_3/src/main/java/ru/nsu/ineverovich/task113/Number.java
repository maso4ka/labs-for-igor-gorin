package ru.nsu.ineverovich.task113;

import java.util.Map;

/**
 * Выражение, представляющее числовую константу.
 */
public class Number extends Expression {
    private final int value;

    /**
     * Создаёт числовую константу.
     *
     * @param value значение константы
     */
    public Number(int value) {
        this.value = value;
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        return value;
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

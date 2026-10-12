package ru.nsu.ineverovich.task113.model;

import java.util.List;

/**
 * Переменная в выражении.
 */
public final class Variable extends Expression {
    private final String name;

    /**
     * Создаёт переменную.
     *
     * @param name имя переменной.
     */
    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println(name);
    }

    @Override
    public int eval(List<String> values) {
        for (String value : values) {
            String[] parts = value.split("=");
            if (parts.length == 2 && parts[0].trim().equals(name)) {
                return Integer.parseInt(parts[1].trim());
            }
        }
        throw new IllegalArgumentException(
                "Не задано значение переменной: " + name);
    }

    @Override
    public Expression derivative(String variable) {
        if (name.equals(variable)) {
            return new Number(1);
        }
        return new Number(0);
    }

    @Override
    public String toString() {
        return name;
    }
}

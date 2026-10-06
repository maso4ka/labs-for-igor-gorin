package ru.nsu.ineverovich.task113;

import java.util.Map;

/**
 * Выражение, представляющее переменную.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Создаёт переменную.
     *
     * @param name имя переменной
     */
    public Variable(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Variable name cannot be empty");
        }
        this.name = name;
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        Integer value = variables.get(name);
        if (value == null) {
            throw new IllegalArgumentException("No value for variable: " + name);
        }
        return value;
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(name.equals(variable) ? 1 : 0);
    }

    @Override
    public String toString() {
        return name;
    }
}

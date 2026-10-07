package ru.nsu.ineverovich.task113.model;

/**
 * Арифметические операции бинарных выражений.
 */
public enum Operation {
    ADD('+'),
    SUB('-'),
    MUL('*'),
    DIV('/');

    private final char symbol;

    Operation(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Возвращает символ операции.
     *
     * @return символ операции.
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Находит операцию по её символу.
     *
     * @param symbol символ операции.
     * @return соответствующая операция.
     */
    public static Operation fromSymbol(char symbol) {
        for (Operation operation : values()) {
            if (operation.symbol == symbol) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Неизвестная операция: " + symbol);
    }
}

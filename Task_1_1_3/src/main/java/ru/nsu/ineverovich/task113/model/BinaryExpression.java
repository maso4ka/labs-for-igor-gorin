package ru.nsu.ineverovich.task113.model;

import java.util.List;

/**
 * Базовый класс для бинарных операций.
 */
public abstract class BinaryExpression extends Expression {
    protected final Expression left;
    protected final Expression right;
    private final Operation operation;

    /**
     * Создаёт бинарное выражение.
     *
     * @param left левый операнд.
     * @param right правый операнд.
     * @param operation операция.
     */
    protected BinaryExpression(Expression left, Expression right, Operation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public void print() {
        System.out.println(toString());
    }

    @Override
    public int eval(List<String> values) {
        int leftValue = left.eval(values);
        int rightValue = right.eval(values);
        return switch (operation) {
            case ADD -> leftValue + rightValue;
            case SUB -> leftValue - rightValue;
            case MUL -> leftValue * rightValue;
            case DIV -> leftValue / rightValue;
        };
    }

    /**
     * Возвращает операцию выражения.
     *
     * @return операция.
     */
    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "(" + left + operation.getSymbol() + right + ")";
    }

}

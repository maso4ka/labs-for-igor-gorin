package ru.nsu.ineverovich.task113.model;

/**
 * Произведение двух выражений.
 */
public final class Mul extends BinaryExpression {
    /**
     * Создаёт произведение.
     *
     * @param left первый множитель.
     * @param right второй множитель.
     */
    public Mul(Expression left, Expression right) {
        super(left, right, Operation.MUL);
    }

    @Override
    public Expression derivative(String variable) {
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))
        );
    }
}

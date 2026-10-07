package ru.nsu.ineverovich.task113.model;

/**
 * Сумма двух выражений.
 */
public final class Add extends BinaryExpression {
    /**
     * Создаёт сумму.
     *
     * @param left левое слагаемое.
     * @param right правое слагаемое.
     */
    public Add(Expression left, Expression right) {
        super(left, right, Operation.ADD);
    }

    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }
}

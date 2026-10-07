package ru.nsu.ineverovich.task113.model;

/**
 * Разность двух выражений.
 */
public final class Sub extends BinaryExpression {
    /**
     * Создаёт разность.
     *
     * @param left уменьшаемое.
     * @param right вычитаемое.
     */
    public Sub(Expression left, Expression right) {
        super(left, right, Operation.SUB);
    }

    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }
}

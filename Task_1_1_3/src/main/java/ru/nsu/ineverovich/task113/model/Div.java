package ru.nsu.ineverovich.task113.model;

/**
 * Частное двух выражений.
 */
public final class Div extends BinaryExpression {
    /**
     * Создаёт частное.
     *
     * @param left делимое.
     * @param right делитель.
     */
    public Div(Expression left, Expression right) {
        super(left, right, Operation.DIV);
    }

    @Override
    public Expression derivative(String variable) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))
                ),
                new Mul(right, right)
        );
    }
}

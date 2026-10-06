package ru.nsu.ineverovich.task113;

/**
 * Операция деления двух выражений.
 */
public class Div extends BinaryExpression {

    /**
     * Создаёт операцию.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    public Div(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Вычисляет производную.
     *
     * @param variable переменная, по которой берётся производная
     * @return производная выражения
     */
    @Override
    public Expression derivative(String variable) {
        return new Div(
                new Sub(new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))),
                new Mul(right, right));
    }

    /**
     * Вычисляет результат операции.
     *
     * @param left левое значение
     * @param right правое значение
     * @return результат операции
     */
    @Override
    protected int operation(int left, int right) {
        return left / right;
    }

    /**
     * Возвращает знак операции.
     *
     * @return знак операции
     */
    @Override
    protected String operator() {
        return "/";
    }

}

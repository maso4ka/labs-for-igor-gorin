package ru.nsu.ineverovich.task113;

/**
 * Операция умножения двух выражений.
 */
public class Mul extends BinaryExpression {

    /**
     * Создаёт операцию.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    public Mul(Expression left, Expression right) {
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
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable)));
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
        return left * right;
    }

    /**
     * Возвращает знак операции.
     *
     * @return знак операции
     */
    @Override
    protected String operator() {
        return "*";
    }

}

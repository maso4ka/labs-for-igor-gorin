package ru.nsu.ineverovich.task113;

/**
 * Операция сложения двух выражений.
 */
public class Sub extends BinaryExpression {

    /**
     * Создаёт операцию.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    public Sub(Expression left, Expression right) {
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
        return new Sub(left.derivative(variable), right.derivative(variable));
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
        return left - right;
    }

    /**
     * Возвращает знак операции.
     *
     * @return знак операции
     */
    @Override
    protected String operator() {
        return "-";
    }

}

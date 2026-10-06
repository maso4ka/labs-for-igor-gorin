package ru.nsu.ineverovich.task113;

import java.util.Map;

/**
 * Базовый класс для бинарных арифметических операций.
 */
public abstract class BinaryExpression extends Expression {
    protected final Expression left;
    protected final Expression right;

    /**
     * Создаёт бинарное выражение.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    protected BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Вычисляет значение бинарного выражения.
     *
     * @param variables значения переменных
     * @return значение выражения
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        return operation(left.eval(variables), right.eval(variables));
    }

    /**
     * Возвращает строковое представление бинарного выражения.
     *
     * @return строковое представление
     */
    @Override
    public String toString() {
        return "(" + left + operator() + right + ")";
    }

    /**
     * Выполняет конкретную бинарную операцию.
     *
     * @param left левое значение
     * @param right правое значение
     * @return результат операции
     */
    protected abstract int operation(int left, int right);

    /**
     * Возвращает знак конкретной операции.
     *
     * @return знак операции
     */
    protected abstract String operator();
}

package ru.nsu.ineverovich.task113;

import java.util.Map;

/**
 * Базовый класс для математических выражений.
 */
public abstract class Expression {

    /**
     * Выводит выражение в консоль.
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Вычисляет значение выражения.
     *
     * @param variables значения переменных
     * @return значение выражения
     */
    public abstract int eval(Map<String, Integer> variables);

    /**
     * Вычисляет производную выражения.
     *
     * @param variable переменная, по которой берётся производная
     * @return новое выражение, являющееся производной
     */
    public abstract Expression derivative(String variable);

    /**
     * Вычисляет значение выражения по строке со значениями переменных.
     *
     * @param assignments присваивания, разделённые точками с запятой
     * @return значение выражения
     */
    public int eval(String assignments) {
        Map<String, Integer> variables = Parser.parseAssignments(assignments);
        return eval(variables);
    }

    /**
     * Создаёт выражение из строки.
     *
     * @param expression строковое представление выражения
     * @return созданное выражение
     */
    public static Expression parse(String expression) {
        return Parser.parse(expression);
    }

    /**
     * Возвращает строковое представление выражения.
     *
     * @return строковое представление
     */
    @Override
    public abstract String toString();
}

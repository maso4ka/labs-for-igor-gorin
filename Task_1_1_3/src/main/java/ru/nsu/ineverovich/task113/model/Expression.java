package ru.nsu.ineverovich.task113.model;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.ineverovich.task113.parser.Parser;

/**
 * Базовый класс для математического выражения.
 */
public abstract class Expression {
    /**
     * Печатает выражение.
     */
    public abstract void print();

    /**
     * Вычисляет значение выражения.
     *
     * @param values означивания переменных.
     * @return значение выражения.
     */
    public abstract int eval(List<String> values);

    /**
     * Вычисляет значение выражения по строке
     * означиваний.
     *
     * @param assignment означивания переменных через точку с
     *                   запятой.
     * @return значение выражения.
     */
    public int eval(String assignment) {
        List<String> values = new ArrayList<>(List.of(assignment.split(";")));
        return eval(values);
    }

    /**
     * Строит производную выражения.
     *
     * @param variable переменная дифференцирования.
     * @return новое выражение-производная.
     */
    public abstract Expression derivative(String variable);

    /**
     * Преобразует выражение в строку.
     *
     * @return строковое представление.
     */
    @Override
    public abstract String toString();

    /**
     * Разбирает строку в математическое выражение.
     *
     * @param input строковое представление выражения.
     * @return построенное выражение.
     */
    public static Expression parse(String input) {
        return Parser.parse(input);
    }
}

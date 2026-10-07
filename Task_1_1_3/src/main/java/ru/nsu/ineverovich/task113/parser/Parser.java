package ru.nsu.ineverovich.task113.parser;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.ineverovich.task113.model.Add;
import ru.nsu.ineverovich.task113.model.BinaryExpression;
import ru.nsu.ineverovich.task113.model.Div;
import ru.nsu.ineverovich.task113.model.Expression;
import ru.nsu.ineverovich.task113.model.Mul;
import ru.nsu.ineverovich.task113.model.Number;
import ru.nsu.ineverovich.task113.model.Operation;
import ru.nsu.ineverovich.task113.model.Sub;
import ru.nsu.ineverovich.task113.model.Variable;

/**
 * Разбирает выражения, записанные в обязательном
 * формате со скобками.
 */
public final class Parser {
    private Parser() {
    }

    /**
     * Разбирает выражение.
     *
     * @param input строковое представление.
     * @return выражение.
     */
    public static Expression parse(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Пустое выражение");
        }
        String cleanedInput = input.replaceAll("\\s+", "");
        Index index = new Index();
        Expression result = parseExpression(cleanedInput, index);
        if (index.value != cleanedInput.length()) {
            throw new IllegalArgumentException("Лишние символы в выражении");
        }
        return result;
    }

    private static Expression parseExpression(String input, Index index) {
        if (index.value >= input.length()) {
            throw new IllegalArgumentException(
                    "Неожиданный конец выражения");
        }

        if (input.charAt(index.value) != '(') {
            return parseValue(input, index);
        }

        index.value++;
        Expression left = parseExpression(input, index);
        if (index.value >= input.length()) {
            throw new IllegalArgumentException("Не найдена операция");
        }
        char operator = input.charAt(index.value++);
        Expression right = parseExpression(input, index);
        if (index.value >= input.length() || input.charAt(index.value) != ')') {
            throw new IllegalArgumentException(
                    "Не найдена закрывающая скобка");
        }
        index.value++;
        return createBinaryExpression(left, right, Operation.fromSymbol(operator));
    }

    private static Expression parseValue(String input, Index index) {
        int start = index.value;
        while (index.value < input.length()
                && input.charAt(index.value) != '('
                && input.charAt(index.value) != ')'
                && input.charAt(index.value) != '+'
                && input.charAt(index.value) != '-'
                && input.charAt(index.value) != '*'
                && input.charAt(index.value) != '/')
        {
            index.value++;
        }

        if (start == index.value) {
            char symbol = input.charAt(index.value);
            if (symbol == '-') {
                index.value++;
                return parseNegativeNumber(input, index);
            }
            throw new IllegalArgumentException("Неизвестный символ: " + symbol);
        }

        String value = input.substring(start, index.value);
        try {
            return new Number(Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return new Variable(value);
        }
    }

    private static Expression parseNegativeNumber(String input, Index index) {
        int start = index.value;
        while (index.value < input.length() && Character.isDigit(input.charAt(index.value))) {
            index.value++;
        }
        if (start == index.value) {
            throw new IllegalArgumentException("После '-' ожидалось число");
        }
        return new Number(-Integer.parseInt(input.substring(start, index.value)));
    }

    private static Expression createBinaryExpression(
            Expression left, Expression right, Operation operation) {
        return switch (operation) {
            case ADD -> new Add(left, right);
            case SUB -> new Sub(left, right);
            case MUL -> new Mul(left, right);
            case DIV -> new Div(left, right);
        };
    }

    private static final class Index {
        private int value;
    }
}

package ru.nsu.ineverovich.task113;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Парсер математических выражений в обязательном формате со скобками.
 */
public final class Parser {
    private Parser() {
    }

    /**
     * Разбирает строку с выражением.
     *
     * @param input строка с выражением
     * @return объект выражения
     */
    public static Expression parse(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }

        String text = input.replaceAll("\\s+", "");
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be empty");
        }

        Index index = new Index();
        Expression result = parseExpression(text, index);

        if (index.value != text.length()) {
            throw new IllegalArgumentException(
                    "Unexpected characters at position " + index.value);
        }

        return result;
    }

    /**
     * Разбирает строку с присваиваниями переменных.
     *
     * @param input строка вида "x = 10; y = 13"
     * @return таблица значений переменных
     */
    public static Map<String, Integer> parseAssignments(String input) {
        Map<String, Integer> result = new HashMap<>();

        if (input == null || input.trim().isEmpty()) {
            return result;
        }

        List<String> assignments = new ArrayList<>(List.of(input.split(";")));
        for (String assignment : assignments) {
            List<String> parts = new ArrayList<>(List.of(assignment.split("=")));
            if (parts.size() != 2) {
                throw new IllegalArgumentException(
                        "Invalid assignment: " + assignment);
            }

            String name = parts.get(0).trim();
            String value = parts.get(1).trim();

            if (!name.matches("[A-Za-z][A-Za-z0-9]*")) {
                throw new IllegalArgumentException(
                        "Invalid variable name: " + name);
            }

            result.put(name, Integer.parseInt(value));
        }

        return result;
    }

    private static Expression parseExpression(String text, Index index) {
        if (index.value >= text.length()) {
            throw new IllegalArgumentException("Unexpected end of expression");
        }

        char current = text.charAt(index.value);

        if (current == '(') {
            index.value++;
            Expression left = parseExpression(text, index);

            if (index.value >= text.length()) {
                throw new IllegalArgumentException("Missing operator");
            }

            char operator = text.charAt(index.value++);
            Expression right = parseExpression(text, index);

            if (index.value >= text.length()
                    || text.charAt(index.value) != ')') {
                throw new IllegalArgumentException("Missing closing parenthesis");
            }
            index.value++;

            return switch (operator) {
                case '+' -> new Add(left, right);
                case '-' -> new Sub(left, right);
                case '*' -> new Mul(left, right);
                case '/' -> new Div(left, right);
                default -> throw new IllegalArgumentException(
                        "Unknown operator: " + operator);
            };
        }

        if (Character.isDigit(current) || current == '-') {
            return parseNumber(text, index);
        }

        if (Character.isLetter(current)) {
            return parseVariable(text, index);
        }

        throw new IllegalArgumentException("Unexpected character: " + current);
    }

    private static Expression parseNumber(String text, Index index) {
        int start = index.value;

        if (text.charAt(index.value) == '-') {
            index.value++;
            if (index.value >= text.length()
                    || !Character.isDigit(text.charAt(index.value))) {
                throw new IllegalArgumentException("Invalid number");
            }
        }

        while (index.value < text.length()
                && Character.isDigit(text.charAt(index.value))) {
            index.value++;
        }

        return new Number(Integer.parseInt(text.substring(start, index.value)));
    }

    private static Expression parseVariable(String text, Index index) {
        int start = index.value;

        while (index.value < text.length()
                && Character.isLetterOrDigit(text.charAt(index.value))) {
            index.value++;
        }

        return new Variable(text.substring(start, index.value));
    }

    private static final class Index {
        private int value;
    }
}

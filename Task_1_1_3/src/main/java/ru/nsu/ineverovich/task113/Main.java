package ru.nsu.ineverovich.task113;

import java.util.Scanner;

import ru.nsu.ineverovich.task113.model.Expression;

/**
 * Точка входа в программу работы с выражениями.
 */
public final class Main {
    private Main() {
    }

    /**
     * Запускает консольную программу.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите выражение: ");
        Expression expression = Expression.parse(scanner.nextLine());

        System.out.println("Выражение: " + expression);
        System.out.print("Введите переменную для производной: ");
        String variable = scanner.nextLine();
        System.out.println("Производная: " + expression.derivative(variable));

        System.out.print("Введите значения переменных ");
        System.out.print("(например, x = 10; y = 13): ");
        String assignment = scanner.nextLine();
        System.out.println("Значение: " + expression.eval(assignment));
    }
}

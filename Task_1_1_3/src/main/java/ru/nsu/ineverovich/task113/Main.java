package ru.nsu.ineverovich.task113;

import java.util.Scanner;

/**
 * Консольное приложение для работы с математическими выражениями.
 */
public final class Main {
    private Main() {
    }

    /**
     * Запускает приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите выражение:");
        Expression expression = Expression.parse(scanner.nextLine());

        System.out.println("Выражение:");
        expression.print();

        System.out.println("Введите переменную для дифференцирования:");
        String variable = scanner.nextLine();

        System.out.println("Производная:");
        expression.derivative(variable).print();

        System.out.println("Введите значения переменных:");
        String assignments = scanner.nextLine();

        System.out.println("Значение: " + expression.eval(assignments));
    }
}

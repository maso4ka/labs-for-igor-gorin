package ru.nsu.ineverovich.task112;

import ru.nsu.ineverovich.task112.ui.ConsoleUi;

/**
 * Запускает консольную версию игры Blackjack.
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
        new ConsoleUi().run();
    }
}

package ru.nsu.ineverovich.task112.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ConsoleUiTest {

    @Test
    void runsGameWithDefaultDeckCount() {
        String output = runGame("\n0\n0\n0\n");

        assertTrue(output.contains("Добро пожаловать в Блекджек!"));
        assertTrue(output.contains("Введите количество колод"));
        assertTrue(output.contains("Раунд 1"));
        assertTrue(output.contains("Игра окончена."));
    }

    @Test
    void usesDefaultDeckCountForInvalidInput() {
        String output = runGame("abc\n0\n0\n0\n");

        assertTrue(output.contains("Введите количество колод"));
        assertTrue(output.contains("Раунд 1"));
        assertTrue(output.contains("Игра окончена."));
    }

    @Test
    void usesDefaultDeckCountForNonPositiveInput() {
        String output = runGame("0\n0\n0\n0\n");

        assertTrue(output.contains("Введите количество колод"));
        assertTrue(output.contains("Раунд 1"));
        assertTrue(output.contains("Игра окончена."));
    }

    @Test
    void acceptsPositiveDeckCount() {
        String output = runGame("2\n0\n0\n0\n");

        assertTrue(output.contains("Раунд 1"));
        assertTrue(output.contains("Игра окончена."));
    }

    @Test
    void printsInvalidContinueMessage() {
        String output = runGame("1\n0\n2\n0\n");

        assertTrue(output.contains("Введите только 1 или 0."));
        assertTrue(output.contains("Игра окончена."));
    }

    private static String runGame(String input) {
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream =
                new PrintStream(outputStream, true, StandardCharsets.UTF_8);

        ConsoleUi consoleUi = new ConsoleUi(inputStream, printStream);
        consoleUi.run();

        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
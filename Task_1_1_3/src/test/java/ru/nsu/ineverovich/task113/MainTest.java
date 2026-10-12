package ru.nsu.ineverovich.task113;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Тесты консольного запуска программы.
 */
class MainTest {
    @Test
    void runsConsoleProgram() {
        String input = "(3+(2*x))\nx\nx = 10\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8)
        );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalIn = System.out;
        java.io.InputStream originalInput = System.in;

        try {
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream, true, StandardCharsets.UTF_8));
            Main.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalIn);
        }

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Выражение: (3+(2*x))"));
        assertTrue(output.contains("Производная:"));
        assertTrue(output.contains("Значение: 23"));
    }
}

package ru.nsu.ineverovich.task122;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.nsu.ineverovich.task122.fixtures.TestConstants.UPDATED_NUMBER;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** Проверяет точный вывод в консоль для примера из задания. */
class MainTest {
    @Test
    void printsTheUpdatedNumber() {
        PrintStream previous = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream captured = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(captured);
            Main.main(new String[] {});
        } finally {
            System.setOut(previous);
        }
        assertEquals(UPDATED_NUMBER + System.lineSeparator(), output.toString(StandardCharsets.UTF_8));
    }
}

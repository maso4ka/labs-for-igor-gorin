package ru.nsu.ineverovich.task131.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Проверяет вывод программы при запуске из командной строки. */
class MainTest {
    @Test
    void printsPositions(@TempDir Path directory) throws IOException {
        Path file = Files.writeString(directory.resolve("input.txt"), "абракадабра",
                StandardCharsets.UTF_8);
        PrintStream previous = System.out;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(result, true, StandardCharsets.UTF_8)) {
            System.setOut(output);
            Main.main(file.toString(), "бра");
        } finally {
            System.setOut(previous);
        }
        assertEquals("[1, 8]" + System.lineSeparator(), result.toString(StandardCharsets.UTF_8));
    }

    @Test
    void printsUsageWhenArgumentsAreMissing() throws IOException {
        PrintStream previous = System.err;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(result, true, StandardCharsets.UTF_8)) {
            System.setErr(output);
            Main.main();
        } finally {
            System.setErr(previous);
        }
        assertTrue(result.toString(StandardCharsets.UTF_8).contains("Main <файл> <подстрока>"));
    }
}

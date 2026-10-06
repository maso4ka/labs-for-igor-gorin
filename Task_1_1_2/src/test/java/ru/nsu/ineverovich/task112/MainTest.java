package ru.nsu.ineverovich.task112;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void startsApplication() {
        String input = "\n0\n0\n0\n";
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream =
                new PrintStream(outputStream, true, StandardCharsets.UTF_8);

        java.io.InputStream oldInput = System.in;
        PrintStream oldOutput = System.out;

        try {
            System.setIn(inputStream);
            System.setOut(printStream);

            Main.main(new String[0]);
        } finally {
            System.setIn(oldInput);
            System.setOut(oldOutput);
        }

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Добро пожаловать в Блекджек!"));
        assertTrue(output.contains("Игра окончена."));
    }
}
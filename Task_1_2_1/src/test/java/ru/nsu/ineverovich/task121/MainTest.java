package ru.nsu.ineverovich.task121;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @TempDir
    Path directory;

    @Test
    void usesFileArgumentForAllRepresentations() throws IOException {
        Path file = directory.resolve("graph.txt");
        Files.writeString(file, "2 1\nA\nB\nA B\n", StandardCharsets.UTF_8);
        String output = run(file.toString());
        assertTrue(output.contains("AdjacencyListGraph"));
        assertTrue(output.contains("AdjacencyMatrixGraph"));
        assertTrue(output.contains("IncidenceMatrixGraph"));
        assertEquals(3, output.split("Кан: \\[A, B\\]", -1).length - 1);
        assertEquals(3, output.split("DFS: \\[A, B\\]", -1).length - 1);
    }

    @Test
    void usesIncludedExampleByDefault() throws IOException {
        String output = run();
        assertTrue(output.contains("Кан: [A, B, C, D, E]"));
        assertTrue(output.contains("Graph{A=[B, C], B=[D], C=[D], D=[E], E=[]}"));
    }

    @Test
    void reportsMissingFile() {
        assertThrows(IOException.class, () -> Main.main(new String[] {
                directory.resolve("missing.txt").toString()}));
    }

    private String run(String... args) throws IOException {
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream captured = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(captured);
            Main.main(args);
        } finally {
            System.setOut(original);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }
}
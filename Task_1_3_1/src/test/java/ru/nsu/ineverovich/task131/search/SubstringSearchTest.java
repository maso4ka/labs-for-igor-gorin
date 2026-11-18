package ru.nsu.ineverovich.task131.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Проверяет поиск подстрок и потоковую обработку файлов. */
class SubstringSearchTest {
    @TempDir
    private Path directory;

    @Test
    void findsOccurrencesFromAssignment() throws IOException {
        assertEquals(List.of(1L, 8L), SubstringSearch.find(writeText("абракадабра"), "бра"));
    }

    @Test
    void findsOverlappingOccurrences() throws IOException {
        assertEquals(List.of(0L, 2L), SubstringSearch.find(writeText("абаба"), "аба"));
    }

    @Test
    void continuesAfterAPartialMatch() throws IOException {
        assertEquals(List.of(5L, 8L), SubstringSearch.find(writeText("aaabaaabaabaab"), "aabaab"));
    }

    @Test
    void countsSupplementaryLettersAsSinglePositions() throws IOException {
        String letter = "𐐀";
        String fileName = writeText(letter + "字" + letter + "字");
        assertEquals(List.of(1L, 3L), SubstringSearch.find(fileName, "字"));
        assertEquals(List.of(0L, 2L), SubstringSearch.find(fileName, letter + "字"));
    }

    @Test
    void findsAnOccurrenceAcrossALineBreak() throws IOException {
        assertEquals(List.of(1L), SubstringSearch.find(writeText("а\r\nба"), "\r\nб"));
    }

    @Test
    void returnsAnEmptyListWhenThereAreNoMatches() throws IOException {
        assertEquals(List.of(), SubstringSearch.find(writeText("абв"), "бвг"));
        assertEquals(List.of(), SubstringSearch.find(writeText(""), "а"));
    }

    @Test
    void findsAnEmptySubstringAtEveryBoundary() throws IOException {
        assertEquals(List.of(0L, 1L, 2L), SubstringSearch.find(writeText("а𐐀"), ""));
        assertEquals(List.of(0L), SubstringSearch.find(writeText(""), ""));
    }

    @Test
    void reportsInvalidUtf8() throws IOException {
        Path file = directory.resolve("invalid.txt");
        try (OutputStream output = Files.newOutputStream(file)) {
            output.write(0xFF);
        }
        assertThrows(MalformedInputException.class,
                () -> SubstringSearch.find(file.toString(), "a"));
    }

    @Test
    void streamsMatchesAcrossBuffersInAGeneratedFile() throws IOException {
        Path file = directory.resolve("large.txt");
        String block = "x".repeat(8191);
        String substring = "needle";
        int blocksBetweenMatches = 1024;
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write(block);
            writer.write(substring);
            for (int i = 0; i < blocksBetweenMatches; i++) {
                writer.write(block);
            }
            writer.write(substring);
        }

        List<Long> positions = new ArrayList<>();
        SubstringSearch.forEachMatch(file.toString(), substring, positions::add);
        long secondPosition = (blocksBetweenMatches + 1L) * block.length() + substring.length();
        assertEquals(List.of((long) block.length(), secondPosition), positions);
    }

    private String writeText(String text) throws IOException {
        return Files.writeString(directory.resolve("input.txt"), text, StandardCharsets.UTF_8)
                .toString();
    }
}

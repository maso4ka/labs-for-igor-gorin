package ru.nsu.ineverovich.task131;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.LongConsumer;

/** Ищет подстроки в UTF-8-файлах алгоритмом Кнута — Морриса — Пратта. */
public final class SubstringSearch {
    private SubstringSearch() {
    }

    /**
     * Возвращает позиции всех вхождений в порядке возрастания, включая перекрывающиеся.
     * Индексы отсчитываются с нуля в кодовых точках Unicode. Пустая подстрока
     * совпадает на каждой границе между символами, в начале и в конце файла.
     * Список занимает O(k) памяти, где k — число вхождений.
     *
     * @param fileName путь к UTF-8-файлу
     * @param substring искомая строка
     * @return список индексов начала вхождений
     * @throws IOException если файл не удалось прочитать или декодировать
     */
    public static List<Long> find(String fileName, String substring) throws IOException {
        List<Long> matches = new ArrayList<>();
        forEachMatch(fileName, substring, matches::add);
        return matches;
    }

    /**
     * Передаёт каждую позицию в обработчик сразу при её обнаружении.
     * Индексы и пустая подстрока обрабатываются по правилам {@link #find(String, String)}.
     * Поиск занимает O(n + m) времени и O(m) памяти, где n и m — длины текста и подстроки.
     *
     * @param fileName путь к UTF-8-файлу
     * @param substring искомая строка
     * @param onMatch обработчик индексов начала вхождений
     * @throws IOException если файл не удалось прочитать или декодировать
     */
    public static void forEachMatch(String fileName, String substring, LongConsumer onMatch)
            throws IOException {
        Objects.requireNonNull(onMatch, "onMatch");
        int[] pattern = substring.codePoints().toArray();
        int[] prefix = prefixLengths(pattern);
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName),
                StandardCharsets.UTF_8)) {
            long position = 0;
            if (pattern.length == 0) {
                do {
                    onMatch.accept(position++);
                } while (readCodePoint(reader) != -1);
                return;
            }

            int matched = 0;
            int codePoint;
            while ((codePoint = readCodePoint(reader)) != -1) {
                matched = extendMatch(pattern, prefix, matched, codePoint);
                if (matched == pattern.length) {
                    onMatch.accept(position - pattern.length + 1);
                    matched = prefix[matched - 1];
                }
                position++;
            }
        }
    }

    private static int[] prefixLengths(int[] pattern) {
        int[] prefix = new int[pattern.length];
        for (int i = 1; i < pattern.length; i++) {
            prefix[i] = extendMatch(pattern, prefix, prefix[i - 1], pattern[i]);
        }
        return prefix;
    }

    private static int extendMatch(int[] pattern, int[] prefix, int matched, int codePoint) {
        while (matched > 0 && codePoint != pattern[matched]) {
            matched = prefix[matched - 1];
        }
        if (codePoint == pattern[matched]) {
            matched++;
        }
        return matched;
    }

    private static int readCodePoint(BufferedReader reader) throws IOException {
        int symbol = reader.read();
        if (symbol != -1 && Character.isHighSurrogate((char) symbol)) {
            return Character.toCodePoint((char) symbol, (char) reader.read());
        }
        return symbol;
    }
}

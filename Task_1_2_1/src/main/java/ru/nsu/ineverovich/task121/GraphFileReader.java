package ru.nsu.ineverovich.task121;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Чтение графа из фиксированного текстового формата. */
public final class GraphFileReader {
    private static final Pattern SEPARATOR = Pattern.compile("(?U)\\s+");

    private GraphFileReader() {
    }

    /**
     * Читает файл и заменяет содержимое переданного графа.
     * Файл целиком проверяется до изменения графа.
     *
     * @param target граф, в который записываются данные
     * @param path путь к файлу в кодировке UTF-8
     * @throws IOException если файл недоступен или имеет неверный формат
     */
    public static void readInto(Graph target, Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            throw new IOException("Файл графа пуст");
        }
        List<String> header = tokens(lines.get(0));
        if (header.size() != 2) {
            throw new IOException("Первая строка должна содержать V и E");
        }
        int vertexCount;
        int edgeCount;
        try {
            vertexCount = Integer.parseInt(header.get(0));
            edgeCount = Integer.parseInt(header.get(1));
        } catch (NumberFormatException exception) {
            throw new IOException("V и E должны быть целыми числами", exception);
        }
        if (vertexCount < 0 || edgeCount < 0
                || lines.size() != 1L + vertexCount + edgeCount) {
            throw new IOException("Неверное число вершин, ребер или строк");
        }

        Graph parsed = new AdjacencyListGraph();
        try {
            for (int i = 0; i < vertexCount; i++) {
                List<String> line = tokens(lines.get(i + 1));
                if (line.size() != 1 || !parsed.addVertex(line.get(0))) {
                    throw new IOException("Неверное или повторное имя вершины в строке " + (i + 2));
                }
            }
            for (int i = 0; i < edgeCount; i++) {
                int lineIndex = vertexCount + i + 1;
                List<String> line = tokens(lines.get(lineIndex));
                if (line.size() != 2 || !parsed.addEdge(line.get(0), line.get(1))) {
                    throw new IOException("Неверное или повторное ребро в строке "
                            + (lineIndex + 1));
                }
            }
        } catch (IllegalArgumentException exception) {
            throw new IOException("Некорректные вершины или ребра", exception);
        }

        target.clear();
        for (String vertex : parsed.getVertices()) {
            target.addVertex(vertex);
        }
        for (String from : parsed.getVertices()) {
            for (String to : parsed.getNeighbors(from)) {
                target.addEdge(from, to);
            }
        }
    }

    private static List<String> tokens(String line) {
        String trimmed = line.strip();
        return trimmed.isEmpty() ? new ArrayList<>()
                : SEPARATOR.splitAsStream(trimmed).collect(Collectors.toCollection(ArrayList::new));
    }
}

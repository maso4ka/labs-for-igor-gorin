package ru.nsu.ineverovich.task121.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import ru.nsu.ineverovich.task121.graph.AdjacencyListGraph;
import ru.nsu.ineverovich.task121.graph.Graph;

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
        Graph parsed = parseGraph(lines);
        replaceGraph(target, parsed);
    }

    /**
     * Читает UTF-8 поток и заменяет содержимое переданного графа.
     * Поток остается открытым; закрыть его должен вызывающий код.
     * Все данные проверяются до изменения графа.
     *
     * @param target граф, в который записываются данные
     * @param input поток с данными графа
     * @throws IOException при ошибке чтения или неверном формате
     */
    public static void readInto(Graph target, InputStream input) throws IOException {
        Graph parsed = parseGraph(readLines(input));
        replaceGraph(target, parsed);
    }

    private static List<String> readLines(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                input, StandardCharsets.UTF_8.newDecoder()));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    private static Graph parseGraph(List<String> lines) throws IOException {
        int vertexCount = readVertexCount(lines);
        Graph parsed = new AdjacencyListGraph();
        try {
            readVertices(parsed, lines, vertexCount);
            readEdges(parsed, lines, vertexCount);
        } catch (IllegalArgumentException exception) {
            throw new IOException("Некорректные вершины или ребра", exception);
        }
        return parsed;
    }

    /** Проверяет заголовок и число строк, возвращая число вершин. */
    private static int readVertexCount(List<String> lines) throws IOException {
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
        return vertexCount;
    }

    private static void readVertices(Graph parsed, List<String> lines, int vertexCount)
            throws IOException {
        for (int lineIndex = 1; lineIndex <= vertexCount; lineIndex++) {
            List<String> line = tokens(lines.get(lineIndex));
            if (line.size() != 1 || !parsed.addVertex(line.get(0))) {
                throw new IOException("Неверное или повторное имя вершины в строке "
                        + (lineIndex + 1));
            }
        }
    }

    private static void readEdges(Graph parsed, List<String> lines, int vertexCount)
            throws IOException {
        for (int lineIndex = vertexCount + 1; lineIndex < lines.size(); lineIndex++) {
            List<String> line = tokens(lines.get(lineIndex));
            if (line.size() != 2 || !parsed.addEdge(line.get(0), line.get(1))) {
                throw new IOException("Неверное или повторное ребро в строке "
                        + (lineIndex + 1));
            }
        }
    }

    private static void replaceGraph(Graph target, Graph parsed) {
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

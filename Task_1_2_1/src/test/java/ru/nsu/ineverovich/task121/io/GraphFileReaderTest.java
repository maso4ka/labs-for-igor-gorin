package ru.nsu.ineverovich.task121.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.nsu.ineverovich.task121.graph.AdjacencyListGraph;
import ru.nsu.ineverovich.task121.graph.AdjacencyMatrixGraph;
import ru.nsu.ineverovich.task121.graph.Graph;
import ru.nsu.ineverovich.task121.graph.IncidenceMatrixGraph;
import ru.nsu.ineverovich.task121.support.TestGraphs;

class GraphFileReaderTest {
    static Stream<Arguments> graphs() {
        return TestGraphs.graphs();
    }

    @TempDir
    Path directory;

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void readsUtf8AndReplacesGraph(String name, Graph graph) throws IOException {
        Path file = write(" 4\t2 \r\nначало\r\nсередина\r\nконец\r\nотдельно\r\n"
                + "начало\tсередина\r\nсередина конец\r\n");
        graph.addVertex("old");
        graph.addEdge("old", "old");
        graph.readFromFile(file);
        assertEquals(List.of("начало", "середина", "конец", "отдельно"), graph.getVertices());
        assertEquals(List.of("середина"), graph.getNeighbors("начало"));
        assertEquals(List.of("конец"), graph.getNeighbors("середина"));
        assertEquals(List.of(), graph.getNeighbors("отдельно"));
        assertFalse(graph.containsVertex("old"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void readsUtf8StreamAndReplacesGraph(String name, Graph graph) throws IOException {
        String content = "2 1\r\nначало\r\nконец\r\nначало конец\r\n";
        graph.addVertex("old");
        try (ByteArrayInputStream input = new ByteArrayInputStream(
                content.getBytes(StandardCharsets.UTF_8))) {
            GraphFileReader.readInto(graph, input);
        }
        assertEquals(List.of("начало", "конец"), graph.getVertices());
        assertEquals(List.of("конец"), graph.getNeighbors("начало"));
        assertEquals(List.of(), graph.getNeighbors("конец"));
    }

    @Test
    void invalidUtf8StreamDoesNotChangeGraph() throws IOException {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex("saved");
        String invalidUtf8 = "1 0\n\u00c3(\n";
        try (ByteArrayInputStream input = new ByteArrayInputStream(
                invalidUtf8.getBytes(StandardCharsets.ISO_8859_1))) {
            assertThrows(IOException.class, () -> GraphFileReader.readInto(graph, input));
        }
        assertEquals(List.of("saved"), graph.getVertices());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void readsEmptyGraphAndSelfLoop(String name, Graph graph) throws IOException {
        graph.addVertex("old");
        graph.readFromFile(write("0 0\n"));
        assertEquals(List.of(), graph.getVertices());
        graph.readFromFile(write("1 1\nA\nA A\n"));
        assertEquals(List.of("A"), graph.getVertices());
        assertTrue(graph.containsEdge("A", "A"));
        assertEquals(List.of("A"), graph.getNeighbors("A"));
    }

    static Stream<String> invalidFiles() {
        return Stream.of(
                "", "\n", "1\n", "1 0 extra\n", "many few\n", "2147483648 0\n",
                "-1 0\n", "0 -1\n", "2147483647 2147483647\n",
                "1 0\n", "0 0\nextra\n", "2 0\nA\nA\n", "1 0\nA B\n",
                "1 0\n\n", "1 0\nA\u00a0B\n", "1 1\nA\nA\n",
                "1 1\nA\nA A extra\n", "1 1\nA\nA missing\n",
                "1 1\nA\nmissing A\n", "2 2\nA\nB\nA B\nA B\n",
                "1 0\nA\n\n");
    }

    @ParameterizedTest
    @MethodSource("invalidFiles")
    void invalidFileDoesNotChangeAnyRepresentation(String content) throws IOException {
        Path file = write(content);
        List<Graph> graphs = List.of(new AdjacencyListGraph(),
                new AdjacencyMatrixGraph(), new IncidenceMatrixGraph());
        for (Graph graph : graphs) {
            TestGraphs.addVertices(graph, "saved", "other");
            graph.addEdge("saved", "other");
            String before = graph.toString();
            assertThrows(IOException.class, () -> graph.readFromFile(file));
            assertEquals(before, graph.toString());
        }
    }

    @Test
    void missingFileDoesNotChangeGraph() {
        Graph graph = new IncidenceMatrixGraph();
        graph.addVertex("saved");
        assertThrows(IOException.class,
                () -> graph.readFromFile(directory.resolve("missing.txt")));
        assertEquals(List.of("saved"), graph.getVertices());
    }

    private Path write(String content) throws IOException {
        Path file = directory.resolve("graph.txt");
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }
}

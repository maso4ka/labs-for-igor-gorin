package ru.nsu.ineverovich.task121;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class GraphFileReaderTest {
    @TempDir
    Path directory;

    @ParameterizedTest(name = "{0}")
    @MethodSource("ru.nsu.oop.graph.TestGraphs#graphs")
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
    @MethodSource("ru.nsu.oop.graph.TestGraphs#graphs")
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
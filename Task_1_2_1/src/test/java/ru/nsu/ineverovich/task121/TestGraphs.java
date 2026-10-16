package ru.nsu.ineverovich.task121;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

final class TestGraphs {
    private TestGraphs() {
    }

    static Stream<Arguments> graphs() {
        return Stream.of(
                Arguments.of("список смежности", new AdjacencyListGraph()),
                Arguments.of("матрица смежности", new AdjacencyMatrixGraph()),
                Arguments.of("матрица инцидентности", new IncidenceMatrixGraph()));
    }

    static void addVertices(Graph graph, String... vertices) {
        for (String vertex : vertices) {
            graph.addVertex(vertex);
        }
    }
}

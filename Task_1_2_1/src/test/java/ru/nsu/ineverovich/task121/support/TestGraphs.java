package ru.nsu.ineverovich.task121.support;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import ru.nsu.ineverovich.task121.graph.AdjacencyListGraph;
import ru.nsu.ineverovich.task121.graph.AdjacencyMatrixGraph;
import ru.nsu.ineverovich.task121.graph.Graph;
import ru.nsu.ineverovich.task121.graph.IncidenceMatrixGraph;

/** Общие данные для проверки графов с разными способами хранения. */
public final class TestGraphs {
    private TestGraphs() {
    }

    /**
     * Создает набор пустых графов для параметризованных тестов.
     *
     * @return названия способов хранения и соответствующие графы
     */
    public static Stream<Arguments> graphs() {
        return Stream.of(
                Arguments.of("список смежности", new AdjacencyListGraph()),
                Arguments.of("матрица смежности", new AdjacencyMatrixGraph()),
                Arguments.of("матрица инцидентности", new IncidenceMatrixGraph()));
    }

    /**
     * Добавляет в граф заданные вершины.
     *
     * @param graph граф для заполнения
     * @param vertices имена вершин
     */
    public static void addVertices(Graph graph, String... vertices) {
        for (String vertex : vertices) {
            graph.addVertex(vertex);
        }
    }
}

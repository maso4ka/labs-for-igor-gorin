package ru.nsu.ineverovich.task121;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TopologicalSortTest {
    static Stream<Arguments> variants() {
        return Stream.of(new KahnSort(), new DfsSort()).flatMap(sorter ->
                Stream.of(new AdjacencyListGraph(), new AdjacencyMatrixGraph(),
                        new IncidenceMatrixGraph()).map(graph -> Arguments.of(
                        sorter.getClass().getSimpleName(),
                        graph.getClass().getSimpleName(), sorter, graph)));
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("variants")
    void sortsEmptyGraph(String algorithm, String storage, TopologicalSort sorter, Graph graph) {
        assertEquals(List.of(), sorter.sort(graph));
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("variants")
    void includesAllIsolatedVertices(String algorithm, String storage,
                                     TopologicalSort sorter, Graph graph) {
        TestGraphs.addVertices(graph, "A", "B", "C");
        assertValidOrder(graph, sorter.sort(graph));
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("variants")
    void sortsBranchesAndDisconnectedComponents(String algorithm, String storage,
                                                TopologicalSort sorter, Graph graph) {
        TestGraphs.addVertices(graph, "D", "C", "B", "A", "X", "Y", "isolated");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");
        graph.addEdge("X", "Y");
        graph.addEdge("A", "B");
        String before = graph.toString();
        List<String> result = sorter.sort(graph);
        assertValidOrder(graph, result);
        assertEquals(before, graph.toString());
        assertThrows(UnsupportedOperationException.class, () -> result.add("new"));
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("variants")
    void detectsCycleInDisconnectedGraph(String algorithm, String storage,
                                         TopologicalSort sorter, Graph graph) {
        TestGraphs.addVertices(graph, "free", "X", "Y", "A", "B", "C");
        graph.addEdge("X", "Y");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");
        String before = graph.toString();
        assertThrows(IllegalStateException.class, () -> sorter.sort(graph));
        assertEquals(before, graph.toString());
        graph.removeEdge("C", "A");
        assertValidOrder(graph, sorter.sort(graph));
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("variants")
    void detectsSelfLoop(String algorithm, String storage, TopologicalSort sorter, Graph graph) {
        graph.addVertex("A");
        graph.addEdge("A", "A");
        assertThrows(IllegalStateException.class, () -> sorter.sort(graph));
        graph.removeEdge("A", "A");
        assertEquals(List.of("A"), sorter.sort(graph));
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("variants")
    void sorterCanBeReusedAfterGraphChanges(String algorithm, String storage,
                                            TopologicalSort sorter, Graph graph) {
        TestGraphs.addVertices(graph, "A", "B");
        graph.addEdge("A", "B");
        assertEquals(List.of("A", "B"), sorter.sort(graph));
        graph.removeEdge("A", "B");
        graph.addEdge("B", "A");
        assertEquals(List.of("B", "A"), sorter.sort(graph));
        graph.clear();
        assertEquals(List.of(), sorter.sort(graph));
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("variants")
    void sortsChain(String algorithm, String storage, TopologicalSort sorter, Graph graph) {
        List<String> expected = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            String vertex = "v" + i;
            graph.addVertex(vertex);
            expected.add(vertex);
            if (i > 0) {
                graph.addEdge("v" + (i - 1), vertex);
            }
        }
        assertEquals(expected, sorter.sort(graph));
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("variants")
    void randomDagsSatisfyEveryEdgeConstraint(String algorithm, String storage,
                                              TopologicalSort sorter, Graph graph) {
        Random random = new Random(123);
        for (int example = 0; example < 20; example++) {
            graph.clear();
            List<String> vertices = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                vertices.add("v" + i);
            }
            Collections.shuffle(vertices, random);
            for (String vertex : vertices) {
                graph.addVertex(vertex);
            }
            for (int from = 0; from < 16; from++) {
                for (int to = from + 1; to < 16; to++) {
                    if (random.nextInt(4) == 0) {
                        graph.addEdge("v" + from, "v" + to);
                    }
                }
            }
            assertValidOrder(graph, sorter.sort(graph));
        }
    }

    private static void assertValidOrder(Graph graph, List<String> order) {
        assertEquals(graph.getVertices().size(), order.size());
        assertEquals(Set.copyOf(graph.getVertices()), Set.copyOf(order));
        Map<String, Integer> positions = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            assertNull(positions.put(order.get(i), i), "Повтор вершины в результате");
        }
        for (String from : graph.getVertices()) {
            for (String to : graph.getNeighbors(from)) {
                assertTrue(positions.get(from) < positions.get(to), from + " -> " + to);
            }
        }
    }
}
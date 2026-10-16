package ru.nsu.ineverovich.task121;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GraphTest {
    static Stream<Arguments> graphs() {
        return TestGraphs.graphs();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void addsAndRemovesVertices(String name, Graph graph) {
        assertTrue(graph.getVertices().isEmpty());
        assertTrue(graph.addVertex("A"));
        assertFalse(graph.addVertex("A"));
        TestGraphs.addVertices(graph, "B", "C");
        assertEquals(List.of("A", "B", "C"), graph.getVertices());
        assertFalse(graph.removeVertex("missing"));
        assertTrue(graph.removeVertex("B"));
        assertEquals(List.of("A", "C"), graph.getVertices());
        assertFalse(graph.containsVertex("B"));
        assertTrue(graph.containsVertex("C"));
        assertTrue(graph.removeVertex("A"));
        assertTrue(graph.removeVertex("C"));
        assertTrue(graph.getVertices().isEmpty());
        assertTrue(graph.addVertex("D"));
        assertEquals(List.of("D"), graph.getVertices());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void edgesHaveDirectionAndNoDuplicates(String name, Graph graph) {
        TestGraphs.addVertices(graph, "A", "B", "C");
        assertTrue(graph.addEdge("A", "B"));
        assertFalse(graph.addEdge("A", "B"));
        assertTrue(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("B", "A"));
        assertEquals(List.of("B"), graph.getNeighbors("A"));
        assertEquals(List.of(), graph.getNeighbors("B"));
        assertFalse(graph.removeEdge("A", "C"));
        assertTrue(graph.addEdge("B", "A"));
        assertTrue(graph.addEdge("A", "A"));
        assertFalse(graph.addEdge("A", "A"));
        assertTrue(graph.containsEdge("A", "A"));
        assertEquals(Set.of("A", "B"), Set.copyOf(graph.getNeighbors("A")));
        assertTrue(graph.removeEdge("A", "B"));
        assertFalse(graph.removeEdge("A", "B"));
        assertTrue(graph.containsEdge("B", "A"));
        assertTrue(graph.removeEdge("A", "A"));
        assertFalse(graph.containsEdge("A", "A"));
        assertFalse(graph.removeEdge("A", "A"));
        assertEquals(List.of(), graph.getNeighbors("A"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void removingVertexPreservesOtherEdges(String name, Graph graph) {
        TestGraphs.addVertices(graph, "A", "B", "C", "D", "E");
        List<List<String>> edges = List.of(List.of("A", "B"), List.of("B", "A"),
                List.of("B", "B"), List.of("B", "D"), List.of("E", "B"),
                List.of("A", "C"), List.of("C", "A"), List.of("C", "C"),
                List.of("D", "E"), List.of("E", "D"));
        for (List<String> edge : edges) {
            graph.addEdge(edge.get(0), edge.get(1));
        }
        graph.removeVertex("B");
        assertEquals(List.of("A", "C", "D", "E"), graph.getVertices());
        assertEquals(Set.of("C"), Set.copyOf(graph.getNeighbors("A")));
        assertEquals(Set.of("A", "C"), Set.copyOf(graph.getNeighbors("C")));
        assertEquals(List.of("E"), graph.getNeighbors("D"));
        assertEquals(List.of("D"), graph.getNeighbors("E"));
        graph.removeVertex("A");
        graph.addVertex("F");
        graph.addEdge("E", "F");
        graph.addEdge("F", "C");
        graph.removeVertex("E");
        assertEquals(List.of(), graph.getNeighbors("D"));
        assertEquals(List.of("C"), graph.getNeighbors("F"));
        assertTrue(graph.containsEdge("C", "C"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void returnedListsAreImmutableSnapshots(String name, Graph graph) {
        graph.addVertex("A");
        List<String> vertices = graph.getVertices();
        graph.addVertex("B");
        graph.addEdge("A", "B");
        List<String> neighbors = graph.getNeighbors("A");
        graph.addVertex("C");
        graph.addEdge("A", "C");
        assertEquals(List.of("A"), vertices);
        assertEquals(List.of("B"), neighbors);
        assertThrows(UnsupportedOperationException.class, () -> vertices.add("D"));
        assertThrows(UnsupportedOperationException.class, neighbors::clear);
        assertEquals(3, graph.getVertices().size());
        assertEquals(Set.of("B", "C"), Set.copyOf(graph.getNeighbors("A")));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void rejectsInvalidNames(String name, Graph graph) {
        graph.addVertex("A");
        List<String> invalid = new ArrayList<>();
        invalid.add(null);
        invalid.addAll(List.of("", " ", "A B", "\tA", "A\nB", "A\u00a0B"));
        for (String vertex : invalid) {
            assertThrows(IllegalArgumentException.class, () -> graph.addVertex(vertex));
            assertThrows(IllegalArgumentException.class, () -> graph.removeVertex(vertex));
            assertThrows(IllegalArgumentException.class, () -> graph.containsVertex(vertex));
            assertThrows(IllegalArgumentException.class, () -> graph.getNeighbors(vertex));
            assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", vertex));
        }
        assertEquals(List.of("A"), graph.getVertices());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void rejectsUnknownEdgeEndpoints(String name, Graph graph) {
        graph.addVertex("A");
        assertThrows(IllegalArgumentException.class, () -> graph.getNeighbors("B"));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("B", "A"));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("A", "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("B", "A"));
        assertThrows(IllegalArgumentException.class, () -> graph.containsEdge("A", "B"));
        assertThrows(IllegalArgumentException.class, () -> graph.containsEdge("B", "A"));
        assertEquals(List.of(), graph.getNeighbors("A"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("graphs")
    void clearAllowsReuse(String name, Graph graph) {
        graph.clear();
        TestGraphs.addVertices(graph, "A", "B", "C");
        graph.addEdge("A", "B");
        graph.addEdge("C", "A");
        graph.addEdge("B", "B");
        graph.clear();
        assertEquals("Graph{}", graph.toString());
        TestGraphs.addVertices(graph, "X", "Y");
        graph.addEdge("Y", "X");
        assertEquals(List.of("X"), graph.getNeighbors("Y"));
    }

    @Test
    void equalityIgnoresRepresentationAndInsertionOrder() {
        Graph list = new AdjacencyListGraph();
        Graph adjacency = new AdjacencyMatrixGraph();
        Graph incidence = new IncidenceMatrixGraph();
        TestGraphs.addVertices(list, "A", "B", "C");
        TestGraphs.addVertices(adjacency, "C", "B", "A");
        TestGraphs.addVertices(incidence, "B", "A", "C");
        list.addEdge("A", "B");
        list.addEdge("A", "C");
        adjacency.addEdge("A", "C");
        adjacency.addEdge("A", "B");
        incidence.addEdge("A", "B");
        incidence.addEdge("A", "C");
        assertEquals(list, adjacency);
        assertEquals(adjacency, list);
        assertEquals(adjacency, incidence);
        assertEquals(list, incidence);
        assertEquals(list.hashCode(), incidence.hashCode());
        assertEquals(list.toString(), adjacency.toString());
        assertEquals("Graph{A=[B, C], B=[], C=[]}", list.toString());
        assertEquals(list, list);
        assertNotEquals(null, list);
        assertNotEquals("Graph", list);
        incidence.addVertex("isolated");
        assertNotEquals(list, incidence);
        incidence.removeVertex("isolated");
        incidence.removeEdge("A", "B");
        assertNotEquals(list, incidence);
        incidence.addEdge("B", "A");
        assertNotEquals(list, incidence);
        assertEquals(new AdjacencyListGraph(), new IncidenceMatrixGraph());
    }

    @Test
    void randomOperationsAgreeWithIndependentModel() {
        List<Graph> graphs = List.of(new AdjacencyListGraph(),
                new AdjacencyMatrixGraph(), new IncidenceMatrixGraph());
        Map<String, Set<String>> expected = new LinkedHashMap<>();
        Random random = new Random(42);
        for (int step = 0; step < 1000; step++) {
            String from = "v" + random.nextInt(12);
            String to = "v" + random.nextInt(12);
            int operation = random.nextInt(4);
            if (operation == 0) {
                boolean added = !expected.containsKey(from);
                expected.putIfAbsent(from, new LinkedHashSet<>());
                for (Graph graph : graphs) {
                    assertEquals(added, graph.addVertex(from));
                }
            } else if (operation == 1) {
                boolean removed = expected.remove(from) != null;
                expected.values().forEach(neighbors -> neighbors.remove(from));
                for (Graph graph : graphs) {
                    assertEquals(removed, graph.removeVertex(from));
                }
            } else if (expected.containsKey(from) && expected.containsKey(to)) {
                boolean changed = operation == 2
                        ? expected.get(from).add(to) : expected.get(from).remove(to);
                for (Graph graph : graphs) {
                    boolean actual = operation == 2
                            ? graph.addEdge(from, to) : graph.removeEdge(from, to);
                    assertEquals(changed, actual);
                }
            }
            for (Graph graph : graphs) {
                assertEquals(List.copyOf(expected.keySet()), graph.getVertices());
                for (String vertex : expected.keySet()) {
                    List<String> neighbors = graph.getNeighbors(vertex);
                    assertEquals(expected.get(vertex), Set.copyOf(neighbors));
                    assertEquals(expected.get(vertex).size(), neighbors.size());
                    for (String other : expected.keySet()) {
                        assertEquals(expected.get(vertex).contains(other),
                                graph.containsEdge(vertex, other));
                    }
                }
            }
        }
    }
}
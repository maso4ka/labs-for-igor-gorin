package ru.nsu.ineverovich.task121.sort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import ru.nsu.ineverovich.task121.graph.Graph;

/** Алгоритм Кана: последовательное извлечение вершин без входящих ребер. */
public final class KahnSort implements TopologicalSort {
    /** Создает алгоритм Кана. */
    public KahnSort() {
    }

    @Override
    public List<String> sort(Graph graph) {
        List<String> vertices = graph.getVertices();
        Map<String, Integer> indegree = new HashMap<>();
        for (String vertex : vertices) {
            indegree.put(vertex, 0);
        }
        for (String vertex : vertices) {
            for (String neighbor : graph.getNeighbors(vertex)) {
                indegree.put(neighbor, indegree.get(neighbor) + 1);
            }
        }
        Queue<String> ready = new ArrayDeque<>();
        for (String vertex : vertices) {
            if (indegree.get(vertex) == 0) {
                ready.add(vertex);
            }
        }
        List<String> result = new ArrayList<>();
        while (!ready.isEmpty()) {
            String vertex = ready.remove();
            result.add(vertex);
            for (String neighbor : graph.getNeighbors(vertex)) {
                int remaining = indegree.get(neighbor) - 1;
                indegree.put(neighbor, remaining);
                if (remaining == 0) {
                    ready.add(neighbor);
                }
            }
        }
        if (result.size() != vertices.size()) {
            throw new IllegalStateException("Граф содержит цикл");
        }
        return List.copyOf(result);
    }
}

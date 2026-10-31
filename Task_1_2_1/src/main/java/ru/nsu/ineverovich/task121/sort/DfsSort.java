package ru.nsu.ineverovich.task121.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.ineverovich.task121.graph.Graph;

/** Топологическая сортировка поиском в глубину с обнаружением циклов. */
public final class DfsSort implements TopologicalSort {
    /** Создает алгоритм поиска в глубину. */
    public DfsSort() {
    }

    @Override
    public List<String> sort(Graph graph) {
        Map<String, Integer> colors = new HashMap<>();
        List<String> result = new ArrayList<>();
        for (String vertex : graph.getVertices()) {
            if (colors.getOrDefault(vertex, 0) == 0) {
                visit(graph, vertex, colors, result);
            }
        }
        Collections.reverse(result);
        return List.copyOf(result);
    }

    private void visit(Graph graph, String vertex, Map<String, Integer> colors,
                       List<String> result) {
        colors.put(vertex, 1);
        for (String neighbor : graph.getNeighbors(vertex)) {
            int color = colors.getOrDefault(neighbor, 0);
            if (color == 1) {
                throw new IllegalStateException("Граф содержит цикл");
            }
            if (color == 0) {
                visit(graph, neighbor, colors, result);
            }
        }
        colors.put(vertex, 2);
        result.add(vertex);
    }
}

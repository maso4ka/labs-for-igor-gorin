package ru.nsu.ineverovich.task121;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Граф, хранящий для каждой вершины множество индексов ее соседей. */
public final class AdjacencyListGraph extends AbstractGraph {
    private final List<Set<Integer>> adjacency = new ArrayList<>();

    /** Создает пустой граф со списком смежности. */
    public AdjacencyListGraph() {
    }

    @Override
    protected void addVertexStorage() {
        adjacency.add(new LinkedHashSet<>());
    }

    @Override
    protected void removeVertexStorage(int index) {
        adjacency.remove(index);
        for (int i = 0; i < adjacency.size(); i++) {
            Set<Integer> updated = new LinkedHashSet<>();
            for (int neighbor : adjacency.get(i)) {
                if (neighbor != index) {
                    updated.add(neighbor > index ? neighbor - 1 : neighbor);
                }
            }
            adjacency.set(i, updated);
        }
    }

    @Override
    protected boolean addEdgeStorage(int from, int to) {
        return adjacency.get(from).add(to);
    }

    @Override
    protected boolean removeEdgeStorage(int from, int to) {
        return adjacency.get(from).remove(to);
    }

    @Override
    protected boolean containsEdgeStorage(int from, int to) {
        return adjacency.get(from).contains(to);
    }

    @Override
    protected List<Integer> getNeighborIndices(int index) {
        return new ArrayList<>(adjacency.get(index));
    }
}

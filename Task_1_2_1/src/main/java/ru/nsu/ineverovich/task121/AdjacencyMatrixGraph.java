package ru.nsu.ineverovich.task121;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Граф с матрицей смежности, строки которой хранятся в ArrayList. */
public final class AdjacencyMatrixGraph extends AbstractGraph {
    private final List<List<Boolean>> matrix = new ArrayList<>();

    /** Создает пустой граф с матрицей смежности. */
    public AdjacencyMatrixGraph() {
    }

    @Override
    protected void addVertexStorage() {
        for (List<Boolean> row : matrix) {
            row.add(false);
        }
        matrix.add(new ArrayList<>(Collections.nCopies(matrix.size() + 1, false)));
    }

    @Override
    protected void removeVertexStorage(int index) {
        matrix.remove(index);
        for (List<Boolean> row : matrix) {
            row.remove(index);
        }
    }

    @Override
    protected boolean addEdgeStorage(int from, int to) {
        if (matrix.get(from).get(to)) {
            return false;
        }
        matrix.get(from).set(to, true);
        return true;
    }

    @Override
    protected boolean removeEdgeStorage(int from, int to) {
        if (!matrix.get(from).get(to)) {
            return false;
        }
        matrix.get(from).set(to, false);
        return true;
    }

    @Override
    protected boolean containsEdgeStorage(int from, int to) {
        return matrix.get(from).get(to);
    }

    @Override
    protected List<Integer> getNeighborIndices(int index) {
        List<Integer> neighbors = new ArrayList<>();
        for (int j = 0; j < matrix.size(); j++) {
            if (matrix.get(index).get(j)) {
                neighbors.add(j);
            }
        }
        return neighbors;
    }
}

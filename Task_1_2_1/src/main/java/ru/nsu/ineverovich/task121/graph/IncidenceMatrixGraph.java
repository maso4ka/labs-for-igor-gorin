package ru.nsu.ineverovich.task121.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Граф с матрицей инцидентности. Строки соответствуют вершинам, столбцы ребрам.
 * В столбце обычного ребра начало помечено -1, конец 1; петля помечена 2.
 * Матрица и каждая ее строка хранятся в ArrayList.
 */
public final class IncidenceMatrixGraph extends AbstractGraph {
    private final List<List<Integer>> matrix = new ArrayList<>();
    private int edgeCount;

    /** Создает пустой граф с матрицей инцидентности. */
    public IncidenceMatrixGraph() {
    }

    @Override
    protected void addVertexStorage() {
        matrix.add(new ArrayList<>(Collections.nCopies(edgeCount, 0)));
    }

    @Override
    protected void removeVertexStorage(int index) {
        for (int edge = edgeCount - 1; edge >= 0; edge--) {
            if (matrix.get(index).get(edge) != 0) {
                removeColumn(edge);
            }
        }
        matrix.remove(index);
    }

    @Override
    protected boolean addEdgeStorage(int from, int to) {
        if (findEdge(from, to) >= 0) {
            return false;
        }
        for (List<Integer> row : matrix) {
            row.add(0);
        }
        if (from == to) {
            matrix.get(from).set(edgeCount, 2);
        } else {
            matrix.get(from).set(edgeCount, -1);
            matrix.get(to).set(edgeCount, 1);
        }
        edgeCount++;
        return true;
    }

    @Override
    protected boolean removeEdgeStorage(int from, int to) {
        int edge = findEdge(from, to);
        if (edge < 0) {
            return false;
        }
        removeColumn(edge);
        return true;
    }

    @Override
    protected boolean containsEdgeStorage(int from, int to) {
        return findEdge(from, to) >= 0;
    }

    @Override
    protected List<Integer> getNeighborIndices(int index) {
        List<Integer> neighbors = new ArrayList<>();
        for (int edge = 0; edge < edgeCount; edge++) {
            if (matrix.get(index).get(edge) == 2) {
                neighbors.add(index);
            } else if (matrix.get(index).get(edge) == -1) {
                for (int vertex = 0; vertex < matrix.size(); vertex++) {
                    if (matrix.get(vertex).get(edge) == 1) {
                        neighbors.add(vertex);
                        break;
                    }
                }
            }
        }
        return neighbors;
    }

    private int findEdge(int from, int to) {
        for (int edge = 0; edge < edgeCount; edge++) {
            if (from == to) {
                if (matrix.get(from).get(edge) == 2) {
                    return edge;
                }
            } else if (matrix.get(from).get(edge) == -1 && matrix.get(to).get(edge) == 1) {
                return edge;
            }
        }
        return -1;
    }

    private void removeColumn(int edge) {
        for (List<Integer> row : matrix) {
            row.remove(edge);
        }
        edgeCount--;
    }
}

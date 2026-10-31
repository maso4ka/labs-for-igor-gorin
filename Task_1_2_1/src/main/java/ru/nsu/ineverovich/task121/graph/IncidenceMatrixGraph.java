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
    private static final int NO_INCIDENCE = 0;
    private static final int SOURCE_MARKER = -1;
    private static final int TARGET_MARKER = 1;
    private static final int SELF_LOOP_MARKER = 2;
    private static final int EDGE_NOT_FOUND = -1;

    private final List<List<Integer>> matrix = new ArrayList<>();
    private int edgeCount;

    /** Создает пустой граф с матрицей инцидентности. */
    public IncidenceMatrixGraph() {
    }

    @Override
    protected void addVertexStorage() {
        matrix.add(new ArrayList<>(Collections.nCopies(edgeCount, NO_INCIDENCE)));
    }

    @Override
    protected void removeVertexStorage(int index) {
        for (int edge = edgeCount - 1; edge >= 0; edge--) {
            if (matrix.get(index).get(edge) != NO_INCIDENCE) {
                removeColumn(edge);
            }
        }
        matrix.remove(index);
    }

    @Override
    protected boolean addEdgeStorage(int from, int to) {
        if (findEdge(from, to) != EDGE_NOT_FOUND) {
            return false;
        }
        for (List<Integer> row : matrix) {
            row.add(NO_INCIDENCE);
        }
        if (from == to) {
            matrix.get(from).set(edgeCount, SELF_LOOP_MARKER);
        } else {
            matrix.get(from).set(edgeCount, SOURCE_MARKER);
            matrix.get(to).set(edgeCount, TARGET_MARKER);
        }
        edgeCount++;
        return true;
    }

    @Override
    protected boolean removeEdgeStorage(int from, int to) {
        int edge = findEdge(from, to);
        if (edge == EDGE_NOT_FOUND) {
            return false;
        }
        removeColumn(edge);
        return true;
    }

    @Override
    protected boolean containsEdgeStorage(int from, int to) {
        return findEdge(from, to) != EDGE_NOT_FOUND;
    }

    @Override
    protected List<Integer> getNeighborIndices(int index) {
        List<Integer> neighbors = new ArrayList<>();
        for (int edge = 0; edge < edgeCount; edge++) {
            if (matrix.get(index).get(edge) == SELF_LOOP_MARKER) {
                neighbors.add(index);
            } else if (matrix.get(index).get(edge) == SOURCE_MARKER) {
                for (int vertex = 0; vertex < matrix.size(); vertex++) {
                    if (matrix.get(vertex).get(edge) == TARGET_MARKER) {
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
                if (matrix.get(from).get(edge) == SELF_LOOP_MARKER) {
                    return edge;
                }
            } else if (matrix.get(from).get(edge) == SOURCE_MARKER
                    && matrix.get(to).get(edge) == TARGET_MARKER) {
                return edge;
            }
        }
        return EDGE_NOT_FOUND;
    }

    private void removeColumn(int edge) {
        for (List<Integer> row : matrix) {
            row.remove(edge);
        }
        edgeCount--;
    }
}

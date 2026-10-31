package ru.nsu.ineverovich.task121.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** Общие операции, проверка аргументов и сравнение графов. */
public abstract class AbstractGraph implements Graph {
    private final List<String> vertices = new ArrayList<>();
    private final Map<String, Integer> indices = new HashMap<>();

    /** Создает пустой граф. */
    protected AbstractGraph() {
    }

    @Override
    public final boolean addVertex(String vertex) {
        validateName(vertex);
        if (indices.containsKey(vertex)) {
            return false;
        }
        addVertexStorage();
        indices.put(vertex, vertices.size());
        vertices.add(vertex);
        return true;
    }

    @Override
    public final boolean removeVertex(String vertex) {
        validateName(vertex);
        Integer index = indices.get(vertex);
        if (index == null) {
            return false;
        }
        removeVertexStorage(index);
        vertices.remove((int) index);
        indices.remove(vertex);
        for (int i = index; i < vertices.size(); i++) {
            indices.put(vertices.get(i), i);
        }
        return true;
    }

    @Override
    public final boolean addEdge(String from, String to) {
        return addEdgeStorage(requireVertex(from), requireVertex(to));
    }

    @Override
    public final boolean removeEdge(String from, String to) {
        return removeEdgeStorage(requireVertex(from), requireVertex(to));
    }

    @Override
    public final boolean containsVertex(String vertex) {
        validateName(vertex);
        return indices.containsKey(vertex);
    }

    @Override
    public final boolean containsEdge(String from, String to) {
        return containsEdgeStorage(requireVertex(from), requireVertex(to));
    }

    @Override
    public final List<String> getVertices() {
        return List.copyOf(vertices);
    }

    @Override
    public final List<String> getNeighbors(String vertex) {
        List<String> neighbors = new ArrayList<>();
        for (int index : getNeighborIndices(requireVertex(vertex))) {
            neighbors.add(vertices.get(index));
        }
        return List.copyOf(neighbors);
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Graph)) {
            return false;
        }
        return signature(this).equals(signature((Graph) other));
    }

    @Override
    public final int hashCode() {
        return signature(this).hashCode();
    }

    @Override
    public final String toString() {
        return "Graph" + signature(this);
    }

    private int requireVertex(String vertex) {
        validateName(vertex);
        Integer index = indices.get(vertex);
        if (index == null) {
            throw new IllegalArgumentException("Нет вершины: " + vertex);
        }
        return index;
    }

    private static void validateName(String vertex) {
        if (vertex == null || vertex.isEmpty()
                || vertex.codePoints().anyMatch(c -> Character.isWhitespace(c)
                || Character.isSpaceChar(c))) {
            throw new IllegalArgumentException("Имя вершины должно быть непустым и без пробелов");
        }
    }

    private static Map<String, Set<String>> signature(Graph graph) {
        Map<String, Set<String>> result = new TreeMap<>();
        for (String vertex : graph.getVertices()) {
            result.put(vertex, new TreeSet<>(graph.getNeighbors(vertex)));
        }
        return result;
    }

    /** Добавляет в хранилище вершину с индексом, равным прежнему числу вершин. */
    protected abstract void addVertexStorage();

    /**
     * Удаляет вершину и ее ребра, уменьшая большие индексы на единицу.
     *
     * @param index индекс удаляемой вершины
     */
    protected abstract void removeVertexStorage(int index);

    /**
     * Добавляет ребро по индексам существующих вершин.
     *
     * @param from начало ребра
     * @param to конец ребра
     * @return было ли добавлено новое ребро
     */
    protected abstract boolean addEdgeStorage(int from, int to);

    /**
     * Удаляет ребро по индексам существующих вершин.
     *
     * @param from начало ребра
     * @param to конец ребра
     * @return было ли удалено ребро
     */
    protected abstract boolean removeEdgeStorage(int from, int to);

    /**
     * Проверяет наличие ребра по индексам существующих вершин.
     *
     * @param from начало ребра
     * @param to конец ребра
     * @return есть ли ребро
     */
    protected abstract boolean containsEdgeStorage(int from, int to);

    /**
     * Возвращает индексы исходящих соседей без повторов.
     *
     * @param index индекс вершины
     * @return список индексов соседей
     */
    protected abstract List<Integer> getNeighborIndices(int index);
}

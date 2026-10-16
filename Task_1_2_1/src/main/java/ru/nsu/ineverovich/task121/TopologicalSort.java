package ru.nsu.ineverovich.task121;

import java.util.List;

/** Алгоритм топологической сортировки, не зависящий от способа хранения графа. */
public interface TopologicalSort {
    /**
     * Находит порядок вершин, в котором начало каждого ребра идет раньше конца.
     * Исходный граф не изменяется.
     *
     * @param graph ориентированный граф
     * @return неизменяемый список всех вершин в топологическом порядке
     * @throws IllegalStateException если граф содержит цикл, включая петлю
     */
    List<String> sort(Graph graph);
}

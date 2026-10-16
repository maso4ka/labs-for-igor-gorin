package ru.nsu.ineverovich.task121;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Ориентированный граф без весов и кратных ребер. Петли разрешены.
 * Имена вершин непустые и не содержат пробельных символов.
 * Соседями считаются вершины, в которые ведут исходящие ребра.
 */
public interface Graph {
    /**
     * Добавляет вершину.
     *
     * @param vertex имя вершины
     * @return true, если вершина добавлена; false, если она уже есть
     * @throws IllegalArgumentException если имя некорректно
     */
    boolean addVertex(String vertex);

    /**
     * Удаляет вершину вместе со всеми входящими и исходящими ребрами.
     *
     * @param vertex имя вершины
     * @return true, если вершина удалена; false, если ее не было
     * @throws IllegalArgumentException если имя некорректно
     */
    boolean removeVertex(String vertex);

    /**
     * Добавляет ребро из from в to. Обе вершины должны существовать.
     *
     * @param from начало ребра
     * @param to конец ребра
     * @return true, если ребро добавлено; false, если оно уже есть
     * @throws IllegalArgumentException если вершины не существует
     */
    boolean addEdge(String from, String to);

    /**
     * Удаляет ребро. Обе вершины должны существовать.
     *
     * @param from начало ребра
     * @param to конец ребра
     * @return true, если ребро удалено; false, если его не было
     * @throws IllegalArgumentException если вершины не существует
     */
    boolean removeEdge(String from, String to);

    /**
     * Проверяет наличие вершины.
     *
     * @param vertex имя вершины
     * @return есть ли вершина в графе
     * @throws IllegalArgumentException если имя некорректно
     */
    boolean containsVertex(String vertex);

    /**
     * Проверяет наличие ребра. Обе вершины должны существовать.
     *
     * @param from начало ребра
     * @param to конец ребра
     * @return есть ли ребро в графе
     * @throws IllegalArgumentException если вершины не существует
     */
    boolean containsEdge(String from, String to);

    /**
     * Возвращает неизменяемую копию списка вершин в порядке добавления.
     *
     * @return список вершин
     */
    List<String> getVertices();

    /**
     * Возвращает неизменяемую копию списка исходящих соседей без повторов.
     * Порядок соседей зависит от способа хранения графа.
     *
     * @param vertex имя вершины
     * @return список соседей
     * @throws IllegalArgumentException если вершины не существует
     */
    List<String> getNeighbors(String vertex);

    /** Удаляет все вершины и ребра. */
    default void clear() {
        for (String vertex : getVertices()) {
            removeVertex(vertex);
        }
    }

    /**
     * Заменяет содержимое графа данными из UTF-8 файла.
     * Первая строка содержит V и E; далее идут V имен и E пар имен.
     * Некорректный файл не меняет содержимое графа.
     *
     * @param path путь к файлу
     * @throws IOException при ошибке чтения или формата
     */
    default void readFromFile(Path path) throws IOException {
        GraphFileReader.readInto(this, path);
    }

    /**
     * Сравнивает имена вершин и направленные ребра, независимо от хранения.
     *
     * @param other сравниваемый объект
     * @return равны ли графы
     */
    @Override
    boolean equals(Object other);

    /**
     * Возвращает хеш, согласованный с равенством графов.
     *
     * @return хеш графа
     */
    @Override
    int hashCode();

    /**
     * Представляет граф в виде отсортированного списка смежности.
     *
     * @return строковое представление графа
     */
    @Override
    String toString();
}

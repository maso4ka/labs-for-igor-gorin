package ru.nsu.ineverovich.task121;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Демонстрация чтения файла и сортировки для всех трех способов хранения. */
public final class Main {
    private Main() {
    }

    /**
     * Запускает демонстрацию.
     *
     * @param args первый аргумент задает файл; по умолчанию examples/graph.txt
     * @throws IOException при ошибке чтения файла
     */
    public static void main(String[] args) throws IOException {
        Path path = Path.of(args.length == 0 ? "examples/graph.txt" : args[0]);
        List<Graph> graphs = List.of(new AdjacencyListGraph(),
                new AdjacencyMatrixGraph(), new IncidenceMatrixGraph());
        for (Graph graph : graphs) {
            graph.readFromFile(path);
            System.out.println(graph.getClass().getSimpleName());
            System.out.println(graph);
            System.out.println("Кан: " + new KahnSort().sort(graph));
            System.out.println("DFS: " + new DfsSort().sort(graph));
            System.out.println();
        }
    }
}
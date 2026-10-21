package ru.nsu.ineverovich.task121;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import ru.nsu.ineverovich.task121.graph.AdjacencyListGraph;
import ru.nsu.ineverovich.task121.graph.AdjacencyMatrixGraph;
import ru.nsu.ineverovich.task121.graph.Graph;
import ru.nsu.ineverovich.task121.graph.IncidenceMatrixGraph;
import ru.nsu.ineverovich.task121.io.GraphFileReader;
import ru.nsu.ineverovich.task121.sort.DfsSort;
import ru.nsu.ineverovich.task121.sort.KahnSort;

/** Демонстрация чтения файла и сортировки для всех трех способов хранения. */
public final class Main {
    private Main() {
    }

    /**
     * Запускает демонстрацию.
     *
     * @param args первый аргумент задает файл; без аргументов читается пример из ресурсов
     * @throws IOException при ошибке чтения файла
     */
    public static void main(String[] args) throws IOException {
        List<Graph> graphs = List.of(new AdjacencyListGraph(),
                new AdjacencyMatrixGraph(), new IncidenceMatrixGraph());
        for (Graph graph : graphs) {
            if (args.length == 0) {
                readExample(graph);
            } else {
                graph.readFromFile(Path.of(args[0]));
            }
            System.out.println(graph.getClass().getSimpleName());
            System.out.println(graph);
            System.out.println("Кан: " + new KahnSort().sort(graph));
            System.out.println("DFS: " + new DfsSort().sort(graph));
            System.out.println();
        }
    }

    private static void readExample(Graph graph) throws IOException {
        try (InputStream input = Main.class.getResourceAsStream("/examples/graph.txt")) {
            if (input == null) {
                throw new IOException("Не найден ресурс examples/graph.txt");
            }
            GraphFileReader.readInto(graph, input);
        }
    }
}

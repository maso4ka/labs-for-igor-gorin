package ru.nsu.ineverovich.task131.cli;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.LongConsumer;
import ru.nsu.ineverovich.task131.search.SubstringSearch;

/** Запускает поиск подстроки в файле из командной строки. */
public final class Main {
    private Main() {
    }

    /**
     * Печатает индексы вхождений в виде списка.
     *
     * @param args путь к UTF-8-файлу и искомая подстрока
     * @throws IOException если файл не удалось прочитать или декодировать
     */
    public static void main(String... args) throws IOException {
        if (args.length != 2) {
            System.err.println("Использование: Main <файл> <подстрока>");
            return;
        }

        PrintWriter output = new PrintWriter(System.out);
        output.print('[');
        SubstringSearch.forEachMatch(args[0], args[1], new LongConsumer() {
            private boolean first = true;

            @Override
            public void accept(long index) {
                if (!first) {
                    output.print(", ");
                }
                output.print(index);
                first = false;
            }
        });
        output.println(']');
        output.flush();
    }
}

package ru.nsu.ineverovich.task122.support;

import static ru.nsu.ineverovich.task122.config.HashTableConstants.EMPTY_HASH_CODE;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import ru.nsu.ineverovich.task122.HashTable;

/** Сравнение содержимого, независимый от порядка хеш и строковое представление. */
public final class TableContracts {
    private TableContracts() {
    }

    /** Сравнивает пары независимо от ёмкости массива и порядка добавления. */
    public static boolean equal(HashTable<?, ?> table, Object other) {
        if (table == other) {
            return true;
        }
        if (!(other instanceof HashTable<?, ?>)) {
            return false;
        }
        HashTable<?, ?> that = (HashTable<?, ?>) other;
        if (table.size() != that.size()) {
            return false;
        }
        for (Map.Entry<?, ?> entry : table) {
            Object key = entry.getKey();
            if (!that.containsKey(key) || !Objects.equals(entry.getValue(), that.get(key))) {
                return false;
            }
        }
        return true;
    }

    /** Суммирует хеши пар, обеспечивая одинаковый хеш равных таблиц. */
    public static int hash(HashTable<?, ?> table) {
        int result = EMPTY_HASH_CODE;
        for (Map.Entry<?, ?> entry : table) {
            result += Objects.hashCode(entry.getKey()) ^ Objects.hashCode(entry.getValue());
        }
        return result;
    }

    /** Выводит пары в строку с обработкой прямой ссылки значения на саму таблицу. */
    public static String format(HashTable<?, ?> table) {
        StringJoiner result = new StringJoiner(", ", "{", "}");
        for (Map.Entry<?, ?> entry : table) {
            result.add(formatPart(entry.getKey(), table) + "="
                    + formatPart(entry.getValue(), table));
        }
        return result.toString();
    }

    private static String formatPart(Object value, HashTable<?, ?> table) {
        return value == table ? "(this HashTable)" : String.valueOf(value);
    }
}

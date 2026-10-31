package ru.nsu.ineverovich.task122;

/** Запускает пример из задания 2.2. */
public final class Main {
    private static final int INITIAL_VALUE = 1;
    private static final double UPDATED_VALUE = 1.0;

    private Main() {
    }

    /** Выводит обновлённое значение: 1.0. */
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", INITIAL_VALUE);
        hashTable.update("one", UPDATED_VALUE);
        System.out.println(hashTable.get("one"));
    }
}

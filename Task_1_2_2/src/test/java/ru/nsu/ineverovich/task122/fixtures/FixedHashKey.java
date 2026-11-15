package ru.nsu.ineverovich.task122.fixtures;

import java.util.Objects;

/** Ключ с заданным хешем для воспроизводимых проверок коллизий. */
public final class FixedHashKey {
    private final String id;
    private final int hash;

    /** Создаёт ключ с заданными именем и хешем. */
    public FixedHashKey(String id, int hash) {
        this.id = id;
        this.hash = hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FixedHashKey)) {
            return false;
        }
        FixedHashKey that = (FixedHashKey) other;
        return hash == that.hash && Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return id;
    }
}

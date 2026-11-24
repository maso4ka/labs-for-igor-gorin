package ru.nsu.ineverovich.task141.model;

import java.util.OptionalInt;

/** Результат оценочного или обычного зачёта, экзамена либо другой работы. */
public enum Grade {
    /** Отлично. */
    EXCELLENT(5, true),
    /** Хорошо. */
    GOOD(4, true),
    /** Удовлетворительно. */
    SATISFACTORY(3, true),
    /** Неудовлетворительно. */
    UNSATISFACTORY(2, false),
    /** Зачтено. */
    PASSED(0, true),
    /** Не зачтено. */
    NOT_PASSED(0, false);

    private final int points;
    private final boolean passing;

    Grade(int points, boolean passing) {
        this.points = points;
        this.passing = passing;
    }

    /**
     * Возвращает балл оценочного результата.
     *
     * @return балл или пустое значение для обычного зачёта
     */
    public OptionalInt points() {
        return points == 0 ? OptionalInt.empty() : OptionalInt.of(points);
    }

    /**
     * Проверяет, что контроль сдан.
     *
     * @return true при положительной оценке или отметке «зачтено»
     */
    public boolean isPassing() {
        return passing;
    }
}

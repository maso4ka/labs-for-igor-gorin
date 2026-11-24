package ru.nsu.ineverovich.task141.model;

/** Виды контроля, указанные в учебном плане. */
public enum AssessmentType {
    /** Задание по дисциплине. */
    ASSIGNMENT(false),
    /** Контрольная работа. */
    TEST(false),
    /** Коллоквиум. */
    COLLOQUIUM(false),
    /** Экзамен. */
    EXAM(true),
    /** Дифференцированный зачёт. */
    GRADED_CREDIT(true),
    /** Обычный зачёт. */
    CREDIT(true),
    /** Защита отчёта по практике. */
    PRACTICE_DEFENSE(true);

    private final boolean sessionAssessment;

    AssessmentType(boolean sessionAssessment) {
        this.sessionAssessment = sessionAssessment;
    }

    /**
     * Проверяет, выставляется ли за контроль числовая оценка.
     *
     * @return true для оценочных видов контроля
     */
    public boolean isGraded() {
        return this != CREDIT;
    }

    /**
     * Проверяет, учитывается ли контроль в результатах сессии.
     *
     * @return true для итоговых аттестаций семестра
     */
    public boolean isSessionAssessment() {
        return sessionAssessment;
    }

    /**
     * Проверяет, может ли оценка войти в расчёт доли пятёрок в приложении к диплому.
     *
     * @return true для оценочных аттестаций семестра
     */
    public boolean isDiplomaAssessment() {
        return sessionAssessment && isGraded();
    }
}

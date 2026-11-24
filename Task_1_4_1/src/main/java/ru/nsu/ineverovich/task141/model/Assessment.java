package ru.nsu.ineverovich.task141.model;

import java.util.Objects;

/** Отдельный контроль по дисциплине в учебном плане. */
public final class Assessment {
    private final String subject;
    private final int semester;
    private final AssessmentType type;
    private final String title;

    /**
     * Создаёт контроль без дополнительного названия.
     *
     * @param subject название дисциплины
     * @param semester номер семестра, начиная с единицы
     * @param type вид контроля
     */
    public Assessment(String subject, int semester, AssessmentType type) {
        this(subject, semester, type, "");
    }

    /**
     * Создаёт контроль с названием, позволяющим различать однотипные работы.
     *
     * @param subject название дисциплины
     * @param semester номер семестра, начиная с единицы
     * @param type вид контроля
     * @param title дополнительное название, например «Контрольная № 2»
     * @throws IllegalArgumentException если дисциплина не названа или номер семестра меньше 1
     */
    public Assessment(String subject, int semester, AssessmentType type, String title) {
        this.subject = Objects.requireNonNull(subject, "subject").strip();
        if (this.subject.isEmpty() || semester < 1) {
            throw new IllegalArgumentException("Укажите дисциплину и положительный номер семестра");
        }
        this.semester = semester;
        this.type = Objects.requireNonNull(type, "type");
        this.title = Objects.requireNonNull(title, "title").strip();
    }

    /**
     * Возвращает название дисциплины.
     *
     * @return название дисциплины
     */
    public String subject() {
        return subject;
    }

    /**
     * Возвращает семестр, в котором запланирован контроль.
     *
     * @return номер семестра
     */
    public int semester() {
        return semester;
    }

    /**
     * Возвращает вид контроля.
     *
     * @return вид контроля
     */
    public AssessmentType type() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Assessment)) {
            return false;
        }
        Assessment assessment = (Assessment) other;
        return semester == assessment.semester
                && type == assessment.type
                && subject.equals(assessment.subject)
                && title.equals(assessment.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, semester, type, title);
    }
}

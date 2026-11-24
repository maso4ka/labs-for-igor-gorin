package ru.nsu.ineverovich.task141.gradebook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import ru.nsu.ineverovich.task141.model.Assessment;
import ru.nsu.ineverovich.task141.model.AssessmentType;
import ru.nsu.ineverovich.task141.model.FundingType;
import ru.nsu.ineverovich.task141.model.Grade;

/** Зачётная книжка студента с учебным планом и выставленными результатами. */
public final class GradeBook {
    private static final double MIN_EXCELLENT_SHARE = 0.75;

    private final Set<Assessment> studyPlan;
    private final Map<Assessment, Result> results = new HashMap<>();
    private FundingType funding;
    private int currentSemester;
    private Grade thesisGrade;

    /**
     * Создаёт зачётку на указанном этапе обучения.
     *
     * @param funding текущая основа обучения
     * @param currentSemester номер текущего семестра, начиная с единицы
     * @param studyPlan все контроли, включая будущие, по плану студента
     * @throws IllegalArgumentException если номер текущего семестра меньше 1
     */
    public GradeBook(FundingType funding, int currentSemester, List<Assessment> studyPlan) {
        if (currentSemester < 1) {
            throw new IllegalArgumentException("Номер текущего семестра должен быть положительным");
        }
        this.funding = Objects.requireNonNull(funding, "funding");
        this.currentSemester = currentSemester;
        this.studyPlan = new LinkedHashSet<>(List.copyOf(studyPlan));
    }

    /**
     * Переводит зачётку на следующий семестр.
     * Сессия предшествующего семестра становится завершённой.
     */
    public void advanceSemester() {
        currentSemester = Math.incrementExact(currentSemester);
    }

    /**
     * Обновляет основу обучения, например после оформленного перевода на бюджет.
     *
     * @param funding новая основа обучения
     */
    public void setFundingType(FundingType funding) {
        this.funding = Objects.requireNonNull(funding, "funding");
    }

    /**
     * Вносит оценку, полученную без пересдачи.
     *
     * @param assessment контроль из учебного плана
     * @param grade актуальный результат
     */
    public void recordGrade(Assessment assessment, Grade grade) {
        recordGrade(assessment, grade, false);
    }

    /**
     * Вносит или заменяет оценку по контролю.
     *
     * @param assessment контроль из текущего или предыдущего семестра
     * @param grade актуальный результат
     * @param retake получен ли результат после пересдачи
     * @throws IllegalArgumentException если контроль вне плана, ещё не наступил его семестр
     *     или вид контроля не допускает переданную оценку
     */
    public void recordGrade(Assessment assessment, Grade grade, boolean retake) {
        Objects.requireNonNull(assessment, "assessment");
        Objects.requireNonNull(grade, "grade");
        if (!studyPlan.contains(assessment) || assessment.semester() > currentSemester) {
            throw new IllegalArgumentException("Контроль должен быть в плане прошедшего "
                    + "или текущего семестра");
        }
        if (assessment.type().isGraded() != grade.points().isPresent()) {
            throw new IllegalArgumentException("Оценка не соответствует виду контроля");
        }
        results.put(assessment, new Result(grade, retake));
    }

    /**
     * Вносит оценку за защиту выпускной квалификационной работы.
     *
     * @param grade числовая оценка за защиту
     * @throws IllegalArgumentException если оценка не является числовой
     */
    public void recordThesisGrade(Grade grade) {
        if (Objects.requireNonNull(grade, "grade").points().isEmpty()) {
            throw new IllegalArgumentException("За ВКР требуется числовая оценка");
        }
        thesisGrade = grade;
    }

    /**
     * Считает среднее всех выставленных числовых оценок, включая защиту ВКР.
     *
     * @return средний балл или пустое значение, если числовых оценок пока нет
     */
    public OptionalDouble averageGrade() {
        IntSummaryStatistics scores = new IntSummaryStatistics();
        for (Result result : results.values()) {
            result.grade.points().ifPresent(scores);
        }
        if (thesisGrade != null) {
            thesisGrade.points().ifPresent(scores);
        }
        return scores.getCount() == 0 ? OptionalDouble.empty()
                : OptionalDouble.of(scores.getAverage());
    }

    /**
     * Проверяет условия перевода на бюджет по зачётке.
     * За экзамены двух последних завершённых сессий требуются четвёрки или пятёрки.
     *
     * @return true, если студент учится платно и выполняет условия по оценкам
     */
    public boolean canTransferToBudget() {
        return funding == FundingType.PAID && currentSemester >= 3
                && examsMeetTransferRule(currentSemester - 2)
                && examsMeetTransferRule(currentSemester - 1);
    }

    /**
     * Проверяет возможность получить диплом с отличием.
     * Для каждой дисциплины учитывается последний оценочный контроль из плана.
     * Для ещё не выставленных итоговых оценок рассматривается лучший случай.
     *
     * @return true, если возможны доля пятёрок от 75 процентов, остальные итоговые четвёрки
     *     и пятёрка за ВКР
     */
    public boolean canGraduateWithHonors() {
        if (thesisGrade != null && thesisGrade != Grade.EXCELLENT) {
            return false;
        }
        Collection<Assessment> finalAssessments = latestDiplomaAssessments();
        int possibleExcellent = 0;
        for (Assessment assessment : finalAssessments) {
            Grade grade = recordedGrade(assessment);
            if (grade == null || grade == Grade.EXCELLENT) {
                possibleExcellent++;
            } else if (grade != Grade.GOOD) {
                return false;
            }
        }
        return !finalAssessments.isEmpty()
                && (double) possibleExcellent / finalAssessments.size() >= MIN_EXCELLENT_SHARE;
    }

    /**
     * Проверяет условия участия в конкурсе на повышенную стипендию по данным зачётки.
     * Учитывает обучение на бюджете со второго курса, последнюю сессию без пересдач
     * с четвёрками и пятёрками, сданные зачёты и отсутствие прежних долгов по сессиям.
     *
     * @return true, если условия участия по данным зачётки выполнены
     */
    public boolean canApplyForHigherScholarship() {
        if (funding != FundingType.BUDGET || currentSemester < 3) {
            return false;
        }
        int previousSemester = currentSemester - 1;
        List<Assessment> session = sessionAssessments(previousSemester);
        return !session.isEmpty() && session.stream().allMatch(this::passesScholarshipRule)
                && studyPlan.stream()
                        .filter(assessment -> assessment.semester() < previousSemester
                                && assessment.type().isSessionAssessment())
                        .allMatch(this::hasPassingGrade);
    }

    private boolean examsMeetTransferRule(int semester) {
        List<Assessment> session = sessionAssessments(semester);
        return !session.isEmpty() && session.stream()
                .filter(assessment -> assessment.type() == AssessmentType.EXAM)
                .map(this::recordedGrade)
                .allMatch(GradeBook::isGoodOrExcellent);
    }

    private List<Assessment> sessionAssessments(int semester) {
        return studyPlan.stream()
                .filter(assessment -> assessment.semester() == semester
                        && assessment.type().isSessionAssessment())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Collection<Assessment> latestDiplomaAssessments() {
        Map<String, Assessment> bySubject = new HashMap<>();
        for (Assessment assessment : studyPlan) {
            if (assessment.type().isDiplomaAssessment()) {
                bySubject.merge(assessment.subject(), assessment,
                        (previous, candidate) -> previous.semester() > candidate.semester()
                                ? previous : candidate);
            }
        }
        return bySubject.values();
    }

    private Grade recordedGrade(Assessment assessment) {
        Result result = results.get(assessment);
        return result == null ? null : result.grade;
    }

    private boolean hasPassingGrade(Assessment assessment) {
        Grade grade = recordedGrade(assessment);
        return grade != null && grade.isPassing();
    }

    private boolean passesScholarshipRule(Assessment assessment) {
        Result result = results.get(assessment);
        return result != null && !result.retake
                && (result.grade == Grade.PASSED || isGoodOrExcellent(result.grade));
    }

    private static boolean isGoodOrExcellent(Grade grade) {
        return grade == Grade.GOOD || grade == Grade.EXCELLENT;
    }

    private static final class Result {
        private final Grade grade;
        private final boolean retake;

        private Result(Grade grade, boolean retake) {
            this.grade = grade;
            this.retake = retake;
        }
    }
}

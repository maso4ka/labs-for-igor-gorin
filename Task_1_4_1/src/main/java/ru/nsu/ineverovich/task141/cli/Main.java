package ru.nsu.ineverovich.task141.cli;

import java.util.List;
import ru.nsu.ineverovich.task141.gradebook.GradeBook;
import ru.nsu.ineverovich.task141.model.Assessment;
import ru.nsu.ineverovich.task141.model.AssessmentType;
import ru.nsu.ineverovich.task141.model.FundingType;
import ru.nsu.ineverovich.task141.model.Grade;

/** Показывает расчёты электронной зачётки на примере учебного плана. */
public final class Main {
    private Main() {
    }

    /**
     * Создаёт зачётку студента третьего семестра и выводит результаты расчётов.
     *
     * @param args аргументы командной строки
     */
    public static void main(String... args) {
        Assessment calculus = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        Assessment programming = new Assessment("Программирование", 2, AssessmentType.EXAM);
        Assessment english = new Assessment("Английский язык", 2, AssessmentType.GRADED_CREDIT);
        Assessment algorithms = new Assessment("Алгоритмы", 3, AssessmentType.EXAM);
        GradeBook book = new GradeBook(FundingType.PAID, 3,
                List.of(calculus, programming, english, algorithms));

        book.recordGrade(calculus, Grade.EXCELLENT);
        book.recordGrade(programming, Grade.EXCELLENT);
        book.recordGrade(english, Grade.GOOD);

        System.out.printf("Средний балл: %.2f%n", book.averageGrade().orElseThrow());
        System.out.println("Перевод на бюджет возможен: " + answer(book.canTransferToBudget()));
        System.out.println("Диплом с отличием возможен: " + answer(book.canGraduateWithHonors()));
        System.out.println("Допуск к конкурсу на повышенную стипендию: "
                + answer(book.canApplyForHigherScholarship()));
    }

    private static String answer(boolean possible) {
        return possible ? "да" : "нет";
    }
}

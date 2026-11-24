package ru.nsu.ineverovich.task141.gradebook;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task141.model.Assessment;
import ru.nsu.ineverovich.task141.model.AssessmentType;
import ru.nsu.ineverovich.task141.model.FundingType;
import ru.nsu.ineverovich.task141.model.Grade;

/** Проверяет правило двух завершённых экзаменационных сессий. */
class BudgetTransferTest {
    @Test
    void checksOnlyExamsFromTheTwoLatestCompletedSessions() {
        Assessment oldExam = new Assessment("Дискретная математика", 1, AssessmentType.EXAM);
        Assessment firstExam = new Assessment("Матанализ", 2, AssessmentType.EXAM);
        Assessment secondExam = new Assessment("Алгоритмы", 3, AssessmentType.EXAM);
        Assessment credit = new Assessment("ООП", 3, AssessmentType.GRADED_CREDIT);
        Assessment currentExam = new Assessment("Теория вероятностей", 4, AssessmentType.EXAM);
        GradeBook book = new GradeBook(FundingType.PAID, 4,
                List.of(oldExam, firstExam, secondExam, credit, currentExam));

        book.recordGrade(oldExam, Grade.SATISFACTORY);
        book.recordGrade(firstExam, Grade.EXCELLENT);
        book.recordGrade(secondExam, Grade.GOOD);
        book.recordGrade(credit, Grade.SATISFACTORY);
        book.recordGrade(currentExam, Grade.SATISFACTORY);

        assertTrue(book.canTransferToBudget());
        book.advanceSemester();
        assertFalse(book.canTransferToBudget());
    }

    @Test
    void requiresPaidStudyAndTwoFinishedSessions() {
        Assessment first = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        Assessment second = new Assessment("Матанализ", 2, AssessmentType.EXAM);
        GradeBook book = new GradeBook(FundingType.PAID, 2, List.of(first, second));
        book.recordGrade(first, Grade.EXCELLENT);
        book.recordGrade(second, Grade.GOOD);

        assertFalse(book.canTransferToBudget());
        book.advanceSemester();
        assertTrue(book.canTransferToBudget());
        book.setFundingType(FundingType.BUDGET);
        assertFalse(book.canTransferToBudget());
    }

    @Test
    void blocksTransferUntilEveryRequiredExamHasAPassingGradeOfFourOrFive() {
        Assessment first = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        Assessment second = new Assessment("Матанализ", 2, AssessmentType.EXAM);
        GradeBook book = new GradeBook(FundingType.PAID, 3, List.of(first, second));
        book.recordGrade(first, Grade.EXCELLENT);

        assertFalse(book.canTransferToBudget());
        book.recordGrade(second, Grade.UNSATISFACTORY);
        assertFalse(book.canTransferToBudget());
        book.recordGrade(second, Grade.GOOD, true);
        assertTrue(book.canTransferToBudget());
    }

    @Test
    void allowsACompletedSessionWithOnlyCredits() {
        Assessment exam = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        Assessment credit = new Assessment("Практикум", 2, AssessmentType.GRADED_CREDIT);
        GradeBook book = new GradeBook(FundingType.PAID, 3, List.of(exam, credit));
        book.recordGrade(exam, Grade.GOOD);
        book.recordGrade(credit, Grade.SATISFACTORY);

        assertTrue(book.canTransferToBudget());
        assertFalse(new GradeBook(FundingType.PAID, 3, List.of()).canTransferToBudget());
    }
}

package ru.nsu.ineverovich.task141.gradebook;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task141.model.Assessment;
import ru.nsu.ineverovich.task141.model.AssessmentType;
import ru.nsu.ineverovich.task141.model.FundingType;
import ru.nsu.ineverovich.task141.model.Grade;

/** Проверяет учебные условия подачи на повышенную стипендию. */
class ScholarshipTest {
    @Test
    void usesThePreviousSessionForBudgetStudentsStartingInTheSecondYear() {
        Assessment first = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        Assessment second = new Assessment("ООП", 2, AssessmentType.GRADED_CREDIT);
        Assessment credit = new Assessment("Физкультура", 2, AssessmentType.CREDIT);
        Assessment current = new Assessment("Алгоритмы", 3, AssessmentType.EXAM);
        GradeBook book = new GradeBook(FundingType.BUDGET, 2,
                List.of(first, second, credit, current));
        book.recordGrade(first, Grade.EXCELLENT);
        book.recordGrade(second, Grade.GOOD);
        book.recordGrade(credit, Grade.PASSED);

        assertFalse(book.canApplyForHigherScholarship());
        book.advanceSemester();
        book.setFundingType(FundingType.PAID);
        assertFalse(book.canApplyForHigherScholarship());

        book.setFundingType(FundingType.BUDGET);
        book.recordGrade(current, Grade.SATISFACTORY);
        assertTrue(book.canApplyForHigherScholarship());
        book.advanceSemester();
        assertFalse(book.canApplyForHigherScholarship());
    }

    @Test
    void requiresTheLatestSessionToBePassedWithoutRetakes() {
        Assessment exam = new Assessment("Матанализ", 2, AssessmentType.EXAM);
        Assessment credit = new Assessment("Физкультура", 2, AssessmentType.CREDIT);
        GradeBook book = new GradeBook(FundingType.BUDGET, 3, List.of(exam, credit));
        book.recordGrade(exam, Grade.EXCELLENT);

        assertFalse(book.canApplyForHigherScholarship());
        book.recordGrade(credit, Grade.NOT_PASSED);
        assertFalse(book.canApplyForHigherScholarship());
        book.recordGrade(credit, Grade.PASSED, true);
        assertFalse(book.canApplyForHigherScholarship());
    }

    @Test
    void checksOpenDebtsFromEarlierSessions() {
        Assessment earlier = new Assessment("Дискретная математика", 1, AssessmentType.EXAM);
        Assessment previous = new Assessment("Матанализ", 2, AssessmentType.EXAM);
        GradeBook book = new GradeBook(FundingType.BUDGET, 3, List.of(earlier, previous));
        book.recordGrade(earlier, Grade.UNSATISFACTORY);
        book.recordGrade(previous, Grade.EXCELLENT);
        assertFalse(book.canApplyForHigherScholarship());

        book.recordGrade(earlier, Grade.SATISFACTORY, true);
        assertTrue(book.canApplyForHigherScholarship());
    }
}

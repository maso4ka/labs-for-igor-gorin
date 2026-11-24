package ru.nsu.ineverovich.task141.gradebook;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.nsu.ineverovich.task141.model.Assessment;
import ru.nsu.ineverovich.task141.model.AssessmentType;
import ru.nsu.ineverovich.task141.model.FundingType;
import ru.nsu.ineverovich.task141.model.Grade;

/** Проверяет прогноз диплома с отличием по всему учебному плану. */
class HonorsTest {
    @Test
    void usesTheExactThresholdAndRecalculatesAfterNewFinalGrades() {
        Assessment first = new Assessment("ООП", 1, AssessmentType.GRADED_CREDIT);
        Assessment second = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        Assessment third = new Assessment("Дискретная математика", 2, AssessmentType.EXAM);
        Assessment fourth = new Assessment("Практика", 3, AssessmentType.PRACTICE_DEFENSE);
        GradeBook book = new GradeBook(FundingType.BUDGET, 1,
                List.of(first, second, third, fourth));
        book.recordGrade(first, Grade.EXCELLENT);
        book.recordGrade(second, Grade.GOOD);

        assertTrue(book.canGraduateWithHonors());
        book.advanceSemester();
        book.recordGrade(third, Grade.GOOD);
        assertFalse(book.canGraduateWithHonors());

        book.recordGrade(third, Grade.EXCELLENT);
        book.advanceSemester();
        book.recordGrade(fourth, Grade.EXCELLENT);
        book.recordThesisGrade(Grade.EXCELLENT);
        assertTrue(book.canGraduateWithHonors());
    }

    @Test
    void selectsTheLastNumericAttestationBySemesterAndPlanOrder() {
        Assessment early = new Assessment("ООП", 1, AssessmentType.GRADED_CREDIT);
        Assessment current = new Assessment("ООП", 2, AssessmentType.GRADED_CREDIT);
        Assessment last = new Assessment("ООП", 2, AssessmentType.EXAM);
        Assessment credit = new Assessment("ООП", 3, AssessmentType.CREDIT);
        Assessment test = new Assessment("ООП", 3, AssessmentType.TEST);
        GradeBook book = new GradeBook(FundingType.BUDGET, 3,
                List.of(current, credit, early, last, test));

        book.recordGrade(early, Grade.SATISFACTORY);
        book.recordGrade(current, Grade.SATISFACTORY);
        book.recordGrade(credit, Grade.PASSED);
        book.recordGrade(test, Grade.SATISFACTORY);
        assertTrue(book.canGraduateWithHonors());

        book.recordGrade(last, Grade.EXCELLENT);
        assertTrue(book.canGraduateWithHonors());
    }

    @ParameterizedTest
    @EnumSource(value = AssessmentType.class, names = {"EXAM", "GRADED_CREDIT"})
    void blocksHonorsWithAFinalSatisfactoryGradeEvenAboveTheThreshold(AssessmentType type) {
        Assessment first = new Assessment("ООП", 1, AssessmentType.EXAM);
        Assessment second = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        Assessment third = new Assessment("Дискретная математика", 1, AssessmentType.EXAM);
        Assessment fourth = new Assessment("История", 1, type);
        GradeBook book = new GradeBook(FundingType.BUDGET, 1,
                List.of(first, second, third, fourth));
        book.recordGrade(first, Grade.EXCELLENT);
        book.recordGrade(second, Grade.EXCELLENT);
        book.recordGrade(third, Grade.EXCELLENT);
        book.recordGrade(fourth, Grade.SATISFACTORY);

        assertFalse(book.canGraduateWithHonors());
    }

    @Test
    void requiresExcellentThesisWhenTheResultIsKnown() {
        Assessment exam = new Assessment("ООП", 1, AssessmentType.EXAM);
        GradeBook book = new GradeBook(FundingType.BUDGET, 1, List.of(exam));
        book.recordGrade(exam, Grade.EXCELLENT);
        assertTrue(book.canGraduateWithHonors());

        book.recordThesisGrade(Grade.GOOD);
        assertFalse(book.canGraduateWithHonors());
        book.recordThesisGrade(Grade.EXCELLENT);
        assertTrue(book.canGraduateWithHonors());
    }

    @Test
    void requiresAtLeastOneDiplomaAssessmentInThePlan() {
        Assessment credit = new Assessment("Физкультура", 1, AssessmentType.CREDIT);
        GradeBook book = new GradeBook(FundingType.BUDGET, 1, List.of(credit));

        assertFalse(book.canGraduateWithHonors());
    }
}

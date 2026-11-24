package ru.nsu.ineverovich.task141.gradebook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task141.model.Assessment;
import ru.nsu.ineverovich.task141.model.AssessmentType;
import ru.nsu.ineverovich.task141.model.FundingType;
import ru.nsu.ineverovich.task141.model.Grade;

/** Проверяет внесение результатов и расчёт среднего балла. */
class GradeBookTest {
    @Test
    void averagesNumericGradesFromAllSemesterRecordsAndThesis() {
        Assessment firstExam = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        Assessment secondExam = new Assessment("Матанализ", 2, AssessmentType.EXAM);
        Assessment credit = new Assessment("Физкультура", 2, AssessmentType.CREDIT);
        Assessment practice = new Assessment("Учебная практика", 2,
                AssessmentType.PRACTICE_DEFENSE);
        GradeBook book = new GradeBook(FundingType.BUDGET, 2,
                List.of(firstExam, secondExam, credit, practice));

        book.recordGrade(firstExam, Grade.SATISFACTORY);
        book.recordGrade(secondExam, Grade.GOOD);
        book.recordGrade(credit, Grade.PASSED);
        book.recordGrade(practice, Grade.EXCELLENT);
        book.recordThesisGrade(Grade.EXCELLENT);

        assertEquals(4.25, book.averageGrade().orElseThrow());
    }

    @Test
    void returnsEmptyAverageUntilThereIsANumericGrade() {
        Assessment credit = new Assessment("Физкультура", 1, AssessmentType.CREDIT);
        GradeBook book = new GradeBook(FundingType.BUDGET, 1, List.of(credit));

        assertTrue(book.averageGrade().isEmpty());
        book.recordGrade(credit, Grade.NOT_PASSED);
        assertTrue(book.averageGrade().isEmpty());
    }

    @Test
    void distinguishesCourseworkAndUpdatesOnlyTheMatchingAssessment() {
        Assessment firstTest = new Assessment("ООП", 1, AssessmentType.TEST, "Контрольная 1");
        Assessment secondTest = new Assessment("ООП", 1, AssessmentType.TEST, "Контрольная 2");
        GradeBook book = new GradeBook(FundingType.PAID, 1, List.of(firstTest, secondTest));

        book.recordGrade(firstTest, Grade.UNSATISFACTORY);
        book.recordGrade(secondTest, Grade.GOOD);
        assertEquals(3.0, book.averageGrade().orElseThrow());

        Assessment sameTest = new Assessment("ООП", 1, AssessmentType.TEST, "Контрольная 1");
        book.recordGrade(sameTest, Grade.EXCELLENT, true);
        assertEquals(4.5, book.averageGrade().orElseThrow());
    }

    @Test
    void keepsItsOwnCopyOfTheStudyPlan() {
        Assessment exam = new Assessment("Матанализ", 1, AssessmentType.EXAM);
        List<Assessment> plan = new ArrayList<>(List.of(exam));
        GradeBook book = new GradeBook(FundingType.PAID, 1, plan);
        plan.clear();

        book.recordGrade(exam, Grade.EXCELLENT);

        assertEquals(5.0, book.averageGrade().orElseThrow());
    }

    @Test
    void validatesAssessmentsAndRejectsResultsOutsideTheCurrentStudyStage() {
        Assessment futureExam = new Assessment("ООП", 2, AssessmentType.EXAM);
        Assessment credit = new Assessment("Физкультура", 1, AssessmentType.CREDIT);
        GradeBook book = new GradeBook(FundingType.PAID, 1, List.of(credit, futureExam));
        Assessment unplanned = new Assessment("Практика", 1, AssessmentType.GRADED_CREDIT);

        assertThrows(IllegalArgumentException.class,
                () -> book.recordGrade(unplanned, Grade.EXCELLENT));
        assertThrows(IllegalArgumentException.class,
                () -> book.recordGrade(futureExam, Grade.EXCELLENT));
        assertThrows(IllegalArgumentException.class,
                () -> book.recordGrade(credit, Grade.GOOD));
        assertThrows(IllegalArgumentException.class, () -> book.recordThesisGrade(Grade.PASSED));
        assertTrue(book.averageGrade().isEmpty());

        book.advanceSemester();
        assertThrows(IllegalArgumentException.class,
                () -> book.recordGrade(futureExam, Grade.PASSED));
        book.recordGrade(futureExam, Grade.GOOD);
        assertEquals(4.0, book.averageGrade().orElseThrow());
    }

    @Test
    void rejectsMissingSubjectNamesAndInvalidSemesterNumbers() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment(" ", 1, AssessmentType.EXAM));
        assertThrows(IllegalArgumentException.class,
                () -> new Assessment("ООП", 0, AssessmentType.EXAM));
        assertThrows(IllegalArgumentException.class,
                () -> new GradeBook(FundingType.PAID, 0, List.of()));
    }
}

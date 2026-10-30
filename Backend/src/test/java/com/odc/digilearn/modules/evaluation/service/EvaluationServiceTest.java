package com.odc.digilearn.modules.evaluation.service;

import com.odc.digilearn.common.audit.AuditService;
import com.odc.digilearn.modules.evaluation.domain.Assessment;
import com.odc.digilearn.modules.evaluation.domain.Grade;
import com.odc.digilearn.modules.evaluation.repository.AssessmentRepository;
import com.odc.digilearn.modules.evaluation.repository.GradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private AuditService auditService;

    private EvaluationService service;

    @BeforeEach
    void setUp() {
        service = new EvaluationService(assessmentRepository, gradeRepository, auditService);
    }

    @Test
    void createsAssessmentWithPositiveScale() {
        UUID trainingId = UUID.randomUUID();
        when(assessmentRepository.save(any(Assessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Assessment assessment = service.createAssessment(trainingId, "Java", 20);

        assertThat(assessment.getTrainingId()).isEqualTo(trainingId);
        assertThat(assessment.getTitle()).isEqualTo("Java");
        assertThat(assessment.getMaximumScore()).isEqualTo(20);
    }

    @Test
    void rejectsNonPositiveScale() {
        assertThatThrownBy(() -> service.createAssessment(UUID.randomUUID(), "Java", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Le barème doit être positif");
    }

    @Test
    void updatesExistingGradeAndAuditsChange() {
        UUID assessmentId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        UUID gradeId = UUID.randomUUID();
        Assessment assessment = new Assessment(UUID.randomUUID(), "Java", 20);
        Grade existing = org.mockito.Mockito.mock(Grade.class);
        when(existing.getId()).thenReturn(gradeId);
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));
        when(gradeRepository.findByAssessmentIdAndLearnerId(assessmentId, learnerId)).thenReturn(Optional.of(existing));
        when(gradeRepository.save(existing)).thenReturn(existing);

        Grade result = service.recordGrade(assessmentId, learnerId, 17, "teacher@example.com");

        assertThat(result).isSameAs(existing);
        verify(existing).updateScore(17);
        verify(auditService).record("teacher@example.com", "GRADE_MODIFIED", gradeId.toString());
    }

    @Test
    void rejectsGradeOutsideAssessmentScale() {
        UUID assessmentId = UUID.randomUUID();
        Assessment assessment = new Assessment(UUID.randomUUID(), "Java", 20);
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        assertThatThrownBy(() -> service.recordGrade(assessmentId, UUID.randomUUID(), 21, "teacher@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Note hors barème");
    }

    @Test
    void reportsGradesForLearner() {
        UUID learnerId = UUID.randomUUID();
        List<Grade> grades = List.of(new Grade(UUID.randomUUID(), learnerId, 15));
        when(gradeRepository.findByLearnerId(learnerId)).thenReturn(grades);

        assertThat(service.report(learnerId)).containsExactlyElementsOf(grades);
    }
}
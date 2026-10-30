package com.odc.digilearn.modules.evaluation.service;

import com.odc.digilearn.common.audit.AuditService;
import com.odc.digilearn.modules.evaluation.domain.Assessment;
import com.odc.digilearn.modules.evaluation.domain.Grade;
import com.odc.digilearn.modules.evaluation.repository.AssessmentRepository;
import com.odc.digilearn.modules.evaluation.repository.GradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EvaluationService {
    private final AssessmentRepository assessmentRepository;
    private final GradeRepository gradeRepository;
    private final AuditService auditService;
    public EvaluationService(AssessmentRepository assessmentRepository, GradeRepository gradeRepository, AuditService auditService) {
        this.assessmentRepository = assessmentRepository; this.gradeRepository = gradeRepository; this.auditService = auditService;
    }
    @Transactional
    public Assessment createAssessment(UUID trainingId, String title, double maximumScore) {
        if (maximumScore <= 0) throw new IllegalArgumentException("Le barème doit être positif");
        return assessmentRepository.save(new Assessment(trainingId, title, maximumScore));
    }
    @Transactional
    public Grade recordGrade(UUID assessmentId, UUID learnerId, double score, String actor) {
        Assessment assessment = assessmentRepository.findById(assessmentId).orElseThrow();
        if (score < 0 || score > assessment.getMaximumScore()) throw new IllegalArgumentException("Note hors barème");
        Grade grade = gradeRepository.findByAssessmentIdAndLearnerId(assessmentId, learnerId)
            .map(existing -> {
                existing.updateScore(score);
                return existing;
            })
            .orElseGet(() -> new Grade(assessmentId, learnerId, score));
        Grade saved = gradeRepository.save(grade);
        auditService.record(actor, "GRADE_MODIFIED", saved.getId().toString());
        return saved;
    }
    public List<Grade> report(UUID learnerId) { return gradeRepository.findByLearnerId(learnerId); }
}

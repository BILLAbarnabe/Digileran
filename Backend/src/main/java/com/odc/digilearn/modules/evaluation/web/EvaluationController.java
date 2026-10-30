package com.odc.digilearn.modules.evaluation.web;

import com.odc.digilearn.modules.evaluation.domain.Assessment;
import com.odc.digilearn.modules.evaluation.domain.Grade;
import com.odc.digilearn.modules.evaluation.service.EvaluationService;
import com.odc.digilearn.security.AuthenticatedUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {
    private final EvaluationService service;
    public EvaluationController(EvaluationService service) { this.service = service; }
    @PostMapping("/assessments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'FORMATEUR')")
    public Assessment createAssessment(@Valid @RequestBody AssessmentRequest request) {
        return service.createAssessment(request.trainingId(), request.title(), request.maximumScore());
    }
    @PostMapping("/grades")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'FORMATEUR')")
    public Grade grade(@AuthenticationPrincipal AuthenticatedUserDetails principal, @Valid @RequestBody GradeRequest request) {
        return service.recordGrade(request.assessmentId(), request.learnerId(), request.score(), principal.user().getEmail());
    }
    @GetMapping("/report/{learnerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'FORMATEUR', 'APPRENANT')")
    public List<Grade> report(@AuthenticationPrincipal AuthenticatedUserDetails principal, @PathVariable UUID learnerId) {
        if (!principal.user().getId().equals(learnerId) && principal.user().getRoles().stream().noneMatch(role ->
                role.name().equals("ADMIN") || role.name().equals("MANAGER") || role.name().equals("FORMATEUR"))) {
            throw new org.springframework.security.access.AccessDeniedException("Bulletin non autorisé");
        }
        return service.report(learnerId);
    }
    public record AssessmentRequest(@NotNull UUID trainingId, @NotBlank String title, double maximumScore) { }
    public record GradeRequest(@NotNull UUID assessmentId, @NotNull UUID learnerId, double score) { }
}

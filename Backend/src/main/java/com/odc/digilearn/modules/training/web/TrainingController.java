package com.odc.digilearn.modules.training.web;

import com.odc.digilearn.modules.training.domain.Training;
import com.odc.digilearn.modules.training.service.TrainingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {
    private final TrainingService service;
    public TrainingController(TrainingService service) { this.service = service; }
    @GetMapping public List<Training> published() { return service.published(); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'FORMATEUR')")
    public Training create(@Valid @RequestBody CreateTrainingRequest request) {
        return service.create(request.title(), request.description());
    }
    @PostMapping("/{id}/publish") @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'FORMATEUR')")
    public void publish(@PathVariable UUID id) { service.publish(id); }
    public record CreateTrainingRequest(@NotBlank String title, String description) { }
}

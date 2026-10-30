package com.odc.digilearn.modules.session.web;

import com.odc.digilearn.modules.session.domain.TrainingSession;
import com.odc.digilearn.modules.session.service.TrainingSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
public class TrainingSessionController {
    private final TrainingSessionService service;
    public TrainingSessionController(TrainingSessionService service) { this.service = service; }

    @GetMapping("/training/{trainingId}")
    public List<TrainingSession> byTraining(@PathVariable UUID trainingId) { return service.byTraining(trainingId); }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'FORMATEUR')")
    public TrainingSession create(@Valid @RequestBody CreateSessionRequest request) {
        return service.create(request.trainingId(), request.title(), request.startsAt(), request.endsAt());
    }

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'FORMATEUR')")
    public ResponseEntity<byte[]> qrCode(@PathVariable UUID id) { return ResponseEntity.ok(service.qrCode(id)); }

    public record CreateSessionRequest(@NotNull UUID trainingId, @NotBlank String title,
                                       @NotNull Instant startsAt, @NotNull Instant endsAt) { }
}

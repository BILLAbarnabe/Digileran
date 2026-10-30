package com.odc.digilearn.modules.candidacy.web;

import com.odc.digilearn.modules.candidacy.domain.Candidacy;
import com.odc.digilearn.modules.candidacy.service.CandidacyService;
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
@RequestMapping("/api/candidacies")
public class CandidacyController {
    private final CandidacyService service;
    public CandidacyController(CandidacyService service) { this.service = service; }
    @PostMapping
    @PreAuthorize("hasRole('APPRENANT')")
    public Candidacy submit(@AuthenticationPrincipal AuthenticatedUserDetails principal,
                            @Valid @RequestBody SubmitRequest request) {
        return service.submit(request.campaignId(), principal.user().getId(), request.cvReference());
    }
    @GetMapping("/mine")
    public List<Candidacy> mine(@AuthenticationPrincipal AuthenticatedUserDetails principal) {
        return service.mine(principal.user().getId());
    }
    @PostMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void accept(@PathVariable UUID id) { service.accept(id); }
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void reject(@PathVariable UUID id) { service.reject(id); }
    public record SubmitRequest(@NotNull UUID campaignId, @NotBlank String cvReference) { }
}

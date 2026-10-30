package com.odc.digilearn.modules.campaign.web;

import com.odc.digilearn.modules.campaign.domain.Campaign;
import com.odc.digilearn.modules.campaign.service.CampaignService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final CampaignService service;
    public CampaignController(CampaignService service) { this.service = service; }
    @GetMapping public List<Campaign> openCampaigns() { return service.openCampaigns(); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Campaign create(@Valid @RequestBody CreateCampaignRequest request) {
        return service.create(request.title(), request.description(), request.openingDate(), request.closingDate());
    }
    @PostMapping("/{id}/close") @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void close(@PathVariable UUID id) { service.close(id); }
    public record CreateCampaignRequest(@NotBlank String title, String description,
                                        @NotNull Instant openingDate, @NotNull Instant closingDate) { }
}

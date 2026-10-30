package com.odc.digilearn.modules.attendance.web;

import com.odc.digilearn.modules.attendance.domain.AttendanceEvent;
import com.odc.digilearn.modules.attendance.service.AttendanceRecordService;
import com.odc.digilearn.security.AuthenticatedUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceRecordService service;
    public AttendanceController(AttendanceRecordService service) { this.service = service; }

    @PostMapping("/{sessionId}/events")
    @PreAuthorize("hasRole('APPRENANT')")
    public AttendanceEvent record(@AuthenticationPrincipal AuthenticatedUserDetails principal,
                                  @PathVariable UUID sessionId, @Valid @RequestBody AttendanceRequest request) {
        return service.record(sessionId, principal.user().getId(), request.connectedAt(), request.disconnectedAt());
    }

    @GetMapping("/{sessionId}/result")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'FORMATEUR', 'APPRENANT')")
    public AttendanceServiceResponse result(@AuthenticationPrincipal AuthenticatedUserDetails principal,
                                             @PathVariable UUID sessionId, @RequestParam long scheduledMinutes) {
        var result = service.evaluate(sessionId, principal.user().getId(), Duration.ofMinutes(scheduledMinutes));
        return new AttendanceServiceResponse(result.connectedSeconds(), result.scheduledSeconds(), result.percentage(), result.validated());
    }

    public record AttendanceRequest(@NotNull Instant connectedAt, @NotNull Instant disconnectedAt) { }
    public record AttendanceServiceResponse(long connectedSeconds, long scheduledSeconds, double percentage, boolean validated) { }
}

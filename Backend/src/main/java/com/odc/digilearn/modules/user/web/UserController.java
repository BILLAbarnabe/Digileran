package com.odc.digilearn.modules.user.web;

import com.odc.digilearn.security.AuthenticatedUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal AuthenticatedUserDetails principal) {
        return new UserResponse(principal.user().getId(), principal.user().getEmail(), principal.user().getRoles());
    }

    @GetMapping("/management-check")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String managementCheck() {
        return "Accès manager autorisé";
    }

    public record UserResponse(java.util.UUID id, String email, java.util.Set<com.odc.digilearn.modules.user.domain.Role> roles) { }
}
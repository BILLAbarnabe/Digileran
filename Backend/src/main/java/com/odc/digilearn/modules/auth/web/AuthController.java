package com.odc.digilearn.modules.auth.web;

import com.odc.digilearn.modules.auth.service.AuthService;
import com.odc.digilearn.security.RefreshTokenCookieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenCookieService cookieService;

    public AuthController(AuthService authService, RefreshTokenCookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody CredentialsRequest request) {
        var result = authService.register(request.email(), request.password());
        return ResponseEntity.ok(new RegistrationResponse(result.email(), result.activationToken()));
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@Valid @RequestBody ActivationRequest request) {
        authService.activate(request.token());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@Valid @RequestBody CredentialsRequest request) {
        var result = authService.login(request.email(), request.password());
        return withRefreshCookie(result.refreshToken(), new AccessTokenResponse(result.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(
            @CookieValue(name = "DIGILEARN_REFRESH_TOKEN", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).build();
        }
        var result = authService.refresh(refreshToken);
        return withRefreshCookie(result.refreshToken(), new AccessTokenResponse(result.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "DIGILEARN_REFRESH_TOKEN", required = false) String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) authService.revoke(refreshToken);
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookieService.clear().toString()).build();
    }

    private <T> ResponseEntity<T> withRefreshCookie(String refreshToken, T body) {
        ResponseCookie cookie = cookieService.create(refreshToken);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(body);
    }

    public record CredentialsRequest(@Email @NotBlank String email, @NotBlank @Size(min = 12, max = 128) String password) { }
    public record ActivationRequest(@NotBlank String token) { }
    public record RegistrationResponse(String email, String activationToken) { }
    public record AccessTokenResponse(String accessToken) { }
}
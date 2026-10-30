package com.odc.digilearn.modules.auth.service;

import com.odc.digilearn.modules.auth.domain.RefreshToken;
import com.odc.digilearn.modules.auth.repository.RefreshTokenRepository;
import com.odc.digilearn.modules.user.domain.Role;
import com.odc.digilearn.modules.user.domain.User;
import com.odc.digilearn.modules.user.repository.UserRepository;
import com.odc.digilearn.security.AuthenticatedUserDetails;
import com.odc.digilearn.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.EnumSet;
import java.util.HexFormat;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long refreshTokenDays;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       @Value("${app.security.refresh-token-days}") long refreshTokenDays) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenDays = refreshTokenDays;
    }

    @Transactional
    public RegistrationResult register(String email, String password) {
        String normalizedEmail = normalize(email);
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée");
        }
        User user = new User(normalizedEmail, passwordEncoder.encode(password), EnumSet.of(Role.APPRENANT));
        String activationToken = randomToken();
        user.setActivation(hash(activationToken), Instant.now().plus(24, ChronoUnit.HOURS));
        userRepository.save(user);
        return new RegistrationResult(user.getEmail(), activationToken);
    }

    @Transactional
    public void activate(String token) {
        String tokenHash = hash(token);
        User user = userRepository.findByActivationTokenHash(tokenHash)
            .filter(candidate -> candidate.activationMatches(tokenHash, Instant.now()))
                .orElseThrow(() -> new IllegalArgumentException("Token d'activation invalide ou expiré"));
        user.activate();
        userRepository.save(user);
    }

    @Transactional
    public LoginResult login(String email, String password) {
        User user = userRepository.findByEmailIgnoreCase(normalize(email))
                .orElseThrow(() -> new IllegalArgumentException("Identifiants invalides"));
        if (!user.isEnabled() || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Identifiants invalides ou compte non activé");
        }
        return issueTokens(user);
    }

    @Transactional
    public LoginResult refresh(String rawRefreshToken) {
        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .orElseThrow(() -> new IllegalArgumentException("Refresh token invalide"));
        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expiré ou révoqué");
        }
        stored.revoke();
        refreshTokenRepository.save(stored);
        User user = userRepository.findByEmailIgnoreCase(stored.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        return issueTokens(user);
    }

    @Transactional
    public void revoke(String rawRefreshToken) {
        refreshTokenRepository.findByTokenHash(hash(rawRefreshToken)).ifPresent(token -> {
            token.revoke();
            refreshTokenRepository.save(token);
        });
    }

    private LoginResult issueTokens(User user) {
        String rawRefreshToken = randomToken();
        refreshTokenRepository.save(new RefreshToken(hash(rawRefreshToken), user.getEmail(),
                Instant.now().plus(refreshTokenDays, ChronoUnit.DAYS)));
        return new LoginResult(jwtService.generateAccessToken(new AuthenticatedUserDetails(user)), rawRefreshToken);
    }

    private String randomToken() {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String normalize(String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email obligatoire");
        return email.toLowerCase().trim();
    }

    private String hash(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception exception) {
            throw new IllegalStateException("Hash indisponible", exception);
        }
    }

    public record RegistrationResult(String email, String activationToken) { }
    public record LoginResult(String accessToken, String refreshToken) { }
}
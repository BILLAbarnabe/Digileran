package com.odc.digilearn.security;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenCookieService {
    private final Duration lifetime;

    public RefreshTokenCookieService(@Value("${app.security.refresh-token-days}") long refreshTokenDays) {
        this.lifetime = Duration.ofDays(refreshTokenDays);
    }

    public ResponseCookie create(String refreshToken) {
        return ResponseCookie.from("DIGILEARN_REFRESH_TOKEN", refreshToken)
                .httpOnly(true).secure(true).sameSite("Strict").path("/api/auth").maxAge(lifetime).build();
    }

    public Cookie clear() {
        Cookie cookie = new Cookie("DIGILEARN_REFRESH_TOKEN", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0);
        return cookie;
    }
}

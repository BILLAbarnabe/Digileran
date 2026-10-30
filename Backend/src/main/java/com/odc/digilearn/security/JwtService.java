package com.odc.digilearn.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey signingKey;
    private final Duration accessTokenLifetime;

    public JwtService(@Value("${app.security.jwt-secret}") String secret,
                      @Value("${app.security.access-token-minutes}") long accessTokenMinutes) {
        byte[] decoded = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(secret.getBytes()));
        this.signingKey = Keys.hmacShaKeyFor(decoded);
        this.accessTokenLifetime = Duration.ofMinutes(accessTokenMinutes);
    }

    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getUsername()).issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessTokenLifetime))).signWith(signingKey).compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean isValid(String token, UserDetails user) {
        try {
            return user.getUsername().equals(extractUsername(token)) &&
                    Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload().getExpiration().after(new Date());
        } catch (RuntimeException exception) {
            return false;
        }
    }
}

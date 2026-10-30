package com.odc.digilearn.modules.user.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 190)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(length = 64)
    private String activationTokenHash;

    private Instant activationExpiresAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles = EnumSet.of(Role.APPRENANT);

    protected User() {
    }

    public User(String email, String passwordHash, Set<Role> roles) {
        this.email = email.toLowerCase().trim();
        this.passwordHash = passwordHash;
        this.roles = EnumSet.copyOf(roles);
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isEnabled() { return enabled; }
    public Set<Role> getRoles() { return roles; }
    public void activate() { this.enabled = true; }
    public void setActivation(String tokenHash, Instant expiresAt) {
        this.activationTokenHash = tokenHash;
        this.activationExpiresAt = expiresAt;
    }
    public boolean activationMatches(String tokenHash, Instant now) {
        return !enabled && activationTokenHash != null && activationTokenHash.equals(tokenHash)
                && activationExpiresAt != null && activationExpiresAt.isAfter(now);
    }
}

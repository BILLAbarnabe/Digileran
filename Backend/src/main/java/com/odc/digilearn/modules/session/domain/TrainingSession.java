package com.odc.digilearn.modules.session.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "training_sessions")
public class TrainingSession {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID trainingId;
    private String title;
    private Instant startsAt;
    private Instant endsAt;
    @jakarta.persistence.Column(nullable = false, unique = true, length = 36)
    private String qrCode;

    protected TrainingSession() { }
    public TrainingSession(UUID trainingId, String title, Instant startsAt, Instant endsAt, String qrCode) {
        this.trainingId = trainingId; this.title = title; this.startsAt = startsAt; this.endsAt = endsAt; this.qrCode = qrCode;
    }
    public UUID getId() { return id; }
    public UUID getTrainingId() { return trainingId; }
    public String getTitle() { return title; }
    public Instant getStartsAt() { return startsAt; }
    public Instant getEndsAt() { return endsAt; }
    public String getQrCode() { return qrCode; }
}

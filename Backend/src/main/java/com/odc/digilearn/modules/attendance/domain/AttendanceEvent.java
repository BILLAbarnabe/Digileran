package com.odc.digilearn.modules.attendance.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "attendance_events")
public class AttendanceEvent {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID sessionId;
    private UUID learnerId;
    private Instant connectedAt;
    private Instant disconnectedAt;
    protected AttendanceEvent() { }
    public AttendanceEvent(UUID sessionId, UUID learnerId, Instant connectedAt, Instant disconnectedAt) {
        this.sessionId = sessionId; this.learnerId = learnerId; this.connectedAt = connectedAt; this.disconnectedAt = disconnectedAt;
    }
    public UUID getId() { return id; }
    public UUID getSessionId() { return sessionId; }
    public UUID getLearnerId() { return learnerId; }
    public Instant getConnectedAt() { return connectedAt; }
    public Instant getDisconnectedAt() { return disconnectedAt; }
}

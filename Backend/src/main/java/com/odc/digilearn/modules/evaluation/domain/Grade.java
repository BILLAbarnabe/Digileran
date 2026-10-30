package com.odc.digilearn.modules.evaluation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "grades")
public class Grade {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID assessmentId;
    private UUID learnerId;
    private double score;
    protected Grade() { }
    public Grade(UUID assessmentId, UUID learnerId, double score) { this.assessmentId = assessmentId; this.learnerId = learnerId; this.score = score; }
    public UUID getId() { return id; }
    public UUID getAssessmentId() { return assessmentId; }
    public UUID getLearnerId() { return learnerId; }
    public double getScore() { return score; }
    public void updateScore(double score) { this.score = score; }
}

package com.odc.digilearn.modules.evaluation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "assessments")
public class Assessment {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID trainingId;
    private String title;
    private double maximumScore;
    protected Assessment() { }
    public Assessment(UUID trainingId, String title, double maximumScore) { this.trainingId = trainingId; this.title = title; this.maximumScore = maximumScore; }
    public UUID getId() { return id; }
    public UUID getTrainingId() { return trainingId; }
    public String getTitle() { return title; }
    public double getMaximumScore() { return maximumScore; }
}

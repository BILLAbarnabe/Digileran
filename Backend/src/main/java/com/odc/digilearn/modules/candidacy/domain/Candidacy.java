package com.odc.digilearn.modules.candidacy.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "candidacies")
public class Candidacy {
    public enum Status { SUBMITTED, REVIEWING, ACCEPTED, REJECTED }
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID campaignId;
    private UUID applicantId;
    private String cvReference;
    private Instant submittedAt;
    @Enumerated(EnumType.STRING)
    private Status status = Status.SUBMITTED;
    protected Candidacy() { }
    public Candidacy(UUID campaignId, UUID applicantId, String cvReference) {
        this.campaignId = campaignId; this.applicantId = applicantId; this.cvReference = cvReference; submittedAt = Instant.now();
    }
    public UUID getId() { return id; }
    public UUID getCampaignId() { return campaignId; }
    public UUID getApplicantId() { return applicantId; }
    public String getCvReference() { return cvReference; }
    public Instant getSubmittedAt() { return submittedAt; }
    public Status getStatus() { return status; }
    public void accept() { status = Status.ACCEPTED; }
    public void reject() { status = Status.REJECTED; }
}

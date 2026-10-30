package com.odc.digilearn.common.retention;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "personal_data_records")
public class PersonalDataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String dataType;

    @Column(nullable = false, length = 190)
    private String ownerReference;

    @Column(columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private Instant collectedAt;

    private Instant anonymizedAt;

    protected PersonalDataRecord() {
    }

    public PersonalDataRecord(String dataType, String ownerReference, String content, Instant collectedAt) {
        this.dataType = dataType;
        this.ownerReference = ownerReference;
        this.content = content;
        this.collectedAt = collectedAt;
    }

    public Instant getCollectedAt() { return collectedAt; }
    public Instant getAnonymizedAt() { return anonymizedAt; }
    public String getContent() { return content; }

    public void anonymize() {
        content = null;
        ownerReference = "ANONYMISED";
        anonymizedAt = Instant.now();
    }
}
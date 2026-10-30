package com.odc.digilearn.modules.campaign.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private Instant openingDate;
    private Instant closingDate;
    private boolean open;

    protected Campaign() { }
    public Campaign(String title, String description, Instant openingDate, Instant closingDate) {
        this.title = title; this.description = description;
        this.openingDate = openingDate; this.closingDate = closingDate; this.open = true;
    }
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Instant getOpeningDate() { return openingDate; }
    public Instant getClosingDate() { return closingDate; }
    public boolean isOpen() { return open; }
    public void close() { open = false; }
}

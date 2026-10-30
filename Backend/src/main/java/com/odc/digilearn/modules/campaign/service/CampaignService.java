package com.odc.digilearn.modules.campaign.service;

import com.odc.digilearn.modules.campaign.domain.Campaign;
import com.odc.digilearn.modules.campaign.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CampaignService {
    private final CampaignRepository repository;
    public CampaignService(CampaignRepository repository) { this.repository = repository; }
    public List<Campaign> openCampaigns() { return repository.findByOpenTrueOrderByOpeningDateDesc(); }
    @Transactional
    public Campaign create(String title, String description, Instant openingDate, Instant closingDate) {
        if (!closingDate.isAfter(openingDate)) throw new IllegalArgumentException("Dates de campagne invalides");
        return repository.save(new Campaign(title, description, openingDate, closingDate));
    }
    @Transactional
    public void close(UUID id) { repository.findById(id).orElseThrow().close(); }
}

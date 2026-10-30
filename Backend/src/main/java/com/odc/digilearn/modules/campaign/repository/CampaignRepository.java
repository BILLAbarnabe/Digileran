package com.odc.digilearn.modules.campaign.repository;

import com.odc.digilearn.modules.campaign.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    List<Campaign> findByOpenTrueOrderByOpeningDateDesc();
}

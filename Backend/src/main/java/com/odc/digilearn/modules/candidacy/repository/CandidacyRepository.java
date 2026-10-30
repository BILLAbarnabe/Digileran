package com.odc.digilearn.modules.candidacy.repository;

import com.odc.digilearn.modules.candidacy.domain.Candidacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidacyRepository extends JpaRepository<Candidacy, UUID> {
    List<Candidacy> findByApplicantIdOrderBySubmittedAtDesc(UUID applicantId);
    boolean existsByCampaignIdAndApplicantId(UUID campaignId, UUID applicantId);
}

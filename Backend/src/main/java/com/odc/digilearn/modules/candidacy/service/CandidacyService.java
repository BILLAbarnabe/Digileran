package com.odc.digilearn.modules.candidacy.service;

import com.odc.digilearn.modules.candidacy.domain.Candidacy;
import com.odc.digilearn.modules.candidacy.repository.CandidacyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CandidacyService {
    private final CandidacyRepository repository;
    public CandidacyService(CandidacyRepository repository) { this.repository = repository; }
    @Transactional
    public Candidacy submit(UUID campaignId, UUID applicantId, String cvReference) {
        if (repository.existsByCampaignIdAndApplicantId(campaignId, applicantId)) {
            throw new IllegalArgumentException("Une candidature existe déjà pour cette campagne");
        }
        return repository.save(new Candidacy(campaignId, applicantId, cvReference));
    }
    public List<Candidacy> mine(UUID applicantId) { return repository.findByApplicantIdOrderBySubmittedAtDesc(applicantId); }
    @Transactional
    public void accept(UUID id) { repository.findById(id).orElseThrow().accept(); }
    @Transactional
    public void reject(UUID id) { repository.findById(id).orElseThrow().reject(); }
}

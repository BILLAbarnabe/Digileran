package com.odc.digilearn.modules.training.service;

import com.odc.digilearn.modules.training.domain.Training;
import com.odc.digilearn.modules.training.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TrainingService {
    private final TrainingRepository repository;
    public TrainingService(TrainingRepository repository) { this.repository = repository; }
    public List<Training> published() { return repository.findByPublishedTrueOrderByTitleAsc(); }
    @Transactional
    public Training create(String title, String description) { return repository.save(new Training(title, description)); }
    @Transactional
    public void publish(UUID id) { repository.findById(id).orElseThrow().publish(); }
}

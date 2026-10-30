package com.odc.digilearn.modules.session.repository;

import com.odc.digilearn.modules.session.domain.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, UUID> {
    List<TrainingSession> findByTrainingIdOrderByStartsAtAsc(UUID trainingId);
}

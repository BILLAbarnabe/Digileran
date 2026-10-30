package com.odc.digilearn.modules.training.repository;

import com.odc.digilearn.modules.training.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrainingRepository extends JpaRepository<Training, UUID> {
    List<Training> findByPublishedTrueOrderByTitleAsc();
}

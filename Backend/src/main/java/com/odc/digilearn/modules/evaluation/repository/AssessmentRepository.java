package com.odc.digilearn.modules.evaluation.repository;

import com.odc.digilearn.modules.evaluation.domain.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> { }

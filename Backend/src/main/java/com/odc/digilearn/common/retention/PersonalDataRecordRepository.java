package com.odc.digilearn.common.retention;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PersonalDataRecordRepository extends JpaRepository<PersonalDataRecord, UUID> {
    List<PersonalDataRecord> findByCollectedAtBeforeAndAnonymizedAtIsNull(Instant cutoff);
}
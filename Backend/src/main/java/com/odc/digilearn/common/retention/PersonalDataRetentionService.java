package com.odc.digilearn.common.retention;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class PersonalDataRetentionService {
    private final long retentionDays;
    private final Clock clock;
    private final PersonalDataRecordRepository repository;

    @Autowired
    public PersonalDataRetentionService(@Value("${app.retention.personal-data-days}") long retentionDays,
                                        PersonalDataRecordRepository repository) {
        this(retentionDays, Clock.systemUTC(), repository);
    }

    PersonalDataRetentionService(long retentionDays, Clock clock, PersonalDataRecordRepository repository) {
        if (retentionDays <= 0) throw new IllegalArgumentException("La durée de conservation doit être positive");
        this.retentionDays = retentionDays;
        this.clock = clock;
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void purgeExpiredPersonalData() {
        Instant cutoff = Instant.now(clock).minus(retentionDays, ChronoUnit.DAYS);
        repository.findByCollectedAtBeforeAndAnonymizedAtIsNull(cutoff).forEach(record -> {
            record.anonymize();
            repository.save(record);
        });
    }
}

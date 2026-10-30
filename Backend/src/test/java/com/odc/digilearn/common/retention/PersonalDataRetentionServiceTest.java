package com.odc.digilearn.common.retention;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalDataRetentionServiceTest {
    private static final Instant NOW = Instant.parse("2026-01-31T12:00:00Z");

    @Mock
    private PersonalDataRecordRepository repository;

    @Test
    void rejectsNonPositiveRetentionPeriod() {
        assertThatThrownBy(() -> new PersonalDataRetentionService(0, Clock.fixed(NOW, ZoneOffset.UTC), repository))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La durée de conservation doit être positive");
    }

    @Test
    void anonymizesRecordsCollectedBeforeCutoff() {
        PersonalDataRecord record = new PersonalDataRecord("email", "user-1", "secret", NOW.minusSeconds(90_000));
        when(repository.findByCollectedAtBeforeAndAnonymizedAtIsNull(NOW.minusSeconds(172_800)))
                .thenReturn(List.of(record));

        new PersonalDataRetentionService(2, Clock.fixed(NOW, ZoneOffset.UTC), repository)
                .purgeExpiredPersonalData();

        assertThat(record.getContent()).isNull();
        assertThat(record.getAnonymizedAt()).isNotNull();
        verify(repository).save(record);
    }

    @Test
    void doesNothingWhenNoRecordIsExpired() {
        when(repository.findByCollectedAtBeforeAndAnonymizedAtIsNull(NOW.minusSeconds(86_400)))
                .thenReturn(List.of());

        new PersonalDataRetentionService(1, Clock.fixed(NOW, ZoneOffset.UTC), repository)
                .purgeExpiredPersonalData();

        verify(repository).findByCollectedAtBeforeAndAnonymizedAtIsNull(NOW.minusSeconds(86_400));
    }
}
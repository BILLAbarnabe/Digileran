package com.odc.digilearn.modules.session.service;

import com.odc.digilearn.modules.session.domain.TrainingSession;
import com.odc.digilearn.modules.session.repository.TrainingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class TrainingSessionServiceTest {
    @Mock
    private TrainingSessionRepository repository;

    private TrainingSessionService service;

    @BeforeEach
    void setUp() {
        service = new TrainingSessionService(repository);
    }

    @Test
    void createsSessionWithUniqueQrValue() {
        UUID trainingId = UUID.randomUUID();
        Instant startsAt = Instant.parse("2026-02-01T09:00:00Z");
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TrainingSession session = service.create(trainingId, "Session 1", startsAt, startsAt.plusSeconds(3_600));

        assertThat(session.getTrainingId()).isEqualTo(trainingId);
        assertThat(session.getQrCode()).isNotBlank();
    }

    @Test
    void rejectsSessionWithEndBeforeStart() {
        Instant startsAt = Instant.parse("2026-02-01T09:00:00Z");

        assertThatThrownBy(() -> service.create(UUID.randomUUID(), "Session 1", startsAt, startsAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dates de séance invalides");
    }

    @Test
    void generatesPngQrCodeForStoredSession() {
        UUID sessionId = UUID.randomUUID();
        TrainingSession session = new TrainingSession(UUID.randomUUID(), "Session 1",
                Instant.parse("2026-02-01T09:00:00Z"), Instant.parse("2026-02-01T10:00:00Z"), "session-token");
        when(repository.findById(any())).thenReturn(Optional.of(session));

        byte[] qrCode = service.qrCode(sessionId);

        assertThat(qrCode).startsWith(new byte[]{(byte) 0x89, 0x50, 0x4e, 0x47});
    }

    @Test
    void delegatesTrainingLookupToRepository() {
        UUID trainingId = UUID.randomUUID();
        List<TrainingSession> sessions = List.of();
        when(repository.findByTrainingIdOrderByStartsAtAsc(trainingId)).thenReturn(sessions);

        assertThat(service.byTraining(trainingId)).isSameAs(sessions);
    }
}
package com.odc.digilearn.modules.session.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatWriter;
import com.odc.digilearn.modules.session.domain.TrainingSession;
import com.odc.digilearn.modules.session.repository.TrainingSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingSessionService {
    private final TrainingSessionRepository repository;
    public TrainingSessionService(TrainingSessionRepository repository) { this.repository = repository; }

    @Transactional
    public TrainingSession create(UUID trainingId, String title, Instant startsAt, Instant endsAt) {
        if (!endsAt.isAfter(startsAt)) throw new IllegalArgumentException("Dates de séance invalides");
        return repository.save(new TrainingSession(trainingId, title, startsAt, endsAt, UUID.randomUUID().toString()));
    }

    public List<TrainingSession> byTraining(UUID trainingId) { return repository.findByTrainingIdOrderByStartsAtAsc(trainingId); }

    public byte[] qrCode(UUID sessionId) {
        TrainingSession session = repository.findById(sessionId).orElseThrow();
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(session.getQrCode(), BarcodeFormat.QR_CODE, 320, 320);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            return output.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible de générer le QR code", exception);
        }
    }
}

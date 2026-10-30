package com.odc.digilearn.modules.attendance.service;

import com.odc.digilearn.modules.attendance.domain.AttendanceEvent;
import com.odc.digilearn.modules.attendance.repository.AttendanceEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AttendanceRecordService {
    private final AttendanceEventRepository repository;
    private final AttendanceService attendanceService;
    public AttendanceRecordService(AttendanceEventRepository repository, AttendanceService attendanceService) {
        this.repository = repository; this.attendanceService = attendanceService;
    }
    @Transactional
    public AttendanceEvent record(UUID sessionId, UUID learnerId, Instant connectedAt, Instant disconnectedAt) {
        if (!disconnectedAt.isAfter(connectedAt)) throw new IllegalArgumentException("Intervalle de présence invalide");
        return repository.save(new AttendanceEvent(sessionId, learnerId, connectedAt, disconnectedAt));
    }
    public AttendanceService.AttendanceResult evaluate(UUID sessionId, UUID learnerId, Duration scheduledDuration) {
        List<AttendanceService.NetworkInterval> intervals = repository.findBySessionIdAndLearnerIdOrderByConnectedAtAsc(sessionId, learnerId)
                .stream().map(event -> new AttendanceService.NetworkInterval(event.getConnectedAt(), event.getDisconnectedAt())).toList();
        return attendanceService.evaluate(scheduledDuration, intervals);
    }
}

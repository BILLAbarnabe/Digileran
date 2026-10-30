package com.odc.digilearn.modules.attendance.repository;

import com.odc.digilearn.modules.attendance.domain.AttendanceEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttendanceEventRepository extends JpaRepository<AttendanceEvent, UUID> {
    List<AttendanceEvent> findBySessionIdAndLearnerIdOrderByConnectedAtAsc(UUID sessionId, UUID learnerId);
}

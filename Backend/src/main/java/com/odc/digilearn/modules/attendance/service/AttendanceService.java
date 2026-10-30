package com.odc.digilearn.modules.attendance.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class AttendanceService {
    public AttendanceResult evaluate(Duration scheduledDuration, List<NetworkInterval> intervals) {
        if (scheduledDuration.isNegative() || scheduledDuration.isZero()) {
            throw new IllegalArgumentException("La durée planifiée doit être positive");
        }
        long connectedSeconds = mergeAndMeasure(intervals);
        long scheduledSeconds = scheduledDuration.toSeconds();
        double percentage = connectedSeconds * 100.0 / scheduledSeconds;
        return new AttendanceResult(connectedSeconds, scheduledSeconds, percentage, percentage >= 80.0);
    }

    private long mergeAndMeasure(List<NetworkInterval> intervals) {
        Instant mergedStart = null;
        Instant mergedEnd = null;
        long totalSeconds = 0;
        for (NetworkInterval interval : intervals.stream().sorted(Comparator.comparing(NetworkInterval::start)).toList()) {
            if (!interval.end().isAfter(interval.start())) {
                continue;
            }
            if (mergedStart == null) {
                mergedStart = interval.start();
                mergedEnd = interval.end();
            } else if (!interval.start().isAfter(mergedEnd)) {
                if (interval.end().isAfter(mergedEnd)) {
                    mergedEnd = interval.end();
                }
            } else {
                totalSeconds += Duration.between(mergedStart, mergedEnd).toSeconds();
                mergedStart = interval.start();
                mergedEnd = interval.end();
            }
        }
        if (mergedStart != null) {
            totalSeconds += Duration.between(mergedStart, mergedEnd).toSeconds();
        }
        return totalSeconds;
    }

    public record NetworkInterval(Instant start, Instant end) { }
    public record AttendanceResult(long connectedSeconds, long scheduledSeconds, double percentage, boolean validated) { }
}

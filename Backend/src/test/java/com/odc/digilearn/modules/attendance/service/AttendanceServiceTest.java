package com.odc.digilearn.modules.attendance.service;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AttendanceServiceTest {
    private final AttendanceService service = new AttendanceService();
    private final Instant start = Instant.parse("2026-01-01T08:00:00Z");

    @Test
    void validatesAtExactlyEightyPercent() {
        var result = service.evaluate(Duration.ofMinutes(100), List.of(
                new AttendanceService.NetworkInterval(start, start.plusSeconds(4_800))));

        assertThat(result.validated()).isTrue();
        assertThat(result.percentage()).isEqualTo(80.0);
    }

    @Test
    void overlappingNetworkIntervalsAreCountedOnce() {
        var result = service.evaluate(Duration.ofMinutes(100), List.of(
                new AttendanceService.NetworkInterval(start, start.plusSeconds(3_000)),
                new AttendanceService.NetworkInterval(start.plusSeconds(2_000), start.plusSeconds(4_800))));

        assertThat(result.connectedSeconds()).isEqualTo(4_800);
        assertThat(result.validated()).isTrue();
    }
}
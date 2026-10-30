package com.odc.digilearn.common.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuditService {
    private static final Logger AUDIT_LOG = LoggerFactory.getLogger("DIGILEARN_AUDIT");

    public void record(String actor, String action, String resourceId) {
        AUDIT_LOG.info("audit timestamp={} actor={} action={} resourceId={}", Instant.now(), actor, action, resourceId);
    }
}

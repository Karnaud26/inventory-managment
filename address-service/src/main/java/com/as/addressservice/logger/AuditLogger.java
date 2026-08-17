package com.as.addressservice.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);

    public void log(String action, UUID entityId, String createdBy, String updatedBy) {
        logger.info("[AUDIT] Action: {}, ID: {}, createdBy: {}, updatedBy: {}", action, entityId, createdBy, updatedBy);
    }
}

package com.easysplit.shared.infrastructure.persistence.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Helper class that contains utility methods to be used for the persistence layer
 */
@Component
public class PersistenceHelper {
    public Timestamp getCurrentDate() {
        return new Timestamp(System.currentTimeMillis());
    }
}

package com.easysplit.shared.infrastructure.persistence.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PersistenceHelper {
    public Timestamp getCurrentDate() {
        return new Timestamp(System.currentTimeMillis());
    }
}

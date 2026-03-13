package com.agrinepal.dao;

import com.agrinepal.model.AuditLog;

public class AuditLogDAO extends FileBasedDAO<AuditLog> {
    public AuditLogDAO() {
        super("data/auditlog.dat");
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Audit records are append-only and non-deletable.");
    }
}

package com.agrinepal.service;

import com.agrinepal.dao.AuditLogDAO;
import com.agrinepal.model.AuditLog;
import com.agrinepal.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AuditService {
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    public void log(String username, String action, String details) {
        AuditLog log = new AuditLog(
                IdGenerator.generate("AUD"),
                LocalDateTime.now(),
                username,
                action,
                details);
        auditLogDAO.save(log);
    }

    public List<AuditLog> search(String keyword) {
        String search = keyword == null ? "" : keyword.toLowerCase();
        return auditLogDAO.findAll().stream()
                .filter(l -> l.getAction().toLowerCase().contains(search)
                        || l.getDetails().toLowerCase().contains(search)
                        || l.getUsername().toLowerCase().contains(search))
                .collect(Collectors.toList());
    }

    public List<AuditLog> all() {
        return auditLogDAO.findAll();
    }
}

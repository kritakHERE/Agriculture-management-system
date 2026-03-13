package com.agrinepal.model;

import java.time.LocalDateTime;

public class AuditLog implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private LocalDateTime timestamp;
    private String username;
    private String action;
    private String details;

    public AuditLog(String id, LocalDateTime timestamp, String username, String action, String details) {
        this.id = id;
        this.timestamp = timestamp;
        this.username = username;
        this.action = action;
        this.details = details;
    }

    @Override
    public String getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }
}

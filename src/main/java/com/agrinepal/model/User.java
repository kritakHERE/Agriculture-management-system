package com.agrinepal.model;

public abstract class User implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String password;
    private String fullName;

    protected User(String id, String username, String password, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public abstract UserRole getRole();

    @Override
    public String toString() {
        return fullName + " (" + getRole() + ")";
    }
}

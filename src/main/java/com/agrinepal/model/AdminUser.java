package com.agrinepal.model;

public class AdminUser extends User {
    private static final long serialVersionUID = 1L;

    public AdminUser(String id, String username, String password, String fullName) {
        super(id, username, password, fullName);
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}

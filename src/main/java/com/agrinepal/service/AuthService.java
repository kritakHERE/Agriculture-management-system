package com.agrinepal.service;

import com.agrinepal.dao.UserDAO;
import com.agrinepal.model.AdminUser;
import com.agrinepal.model.CooperativeOfficerUser;
import com.agrinepal.model.FarmerUser;
import com.agrinepal.model.User;
import com.agrinepal.model.UserRole;
import com.agrinepal.util.IdGenerator;
import com.agrinepal.util.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public AuthService() {
        seedDefaultUsers();
    }

    public Optional<User> login(String username, String password) {
        return userDAO.findAll().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password))
                .findFirst();
    }

    public List<User> allUsers() {
        return userDAO.findAll();
    }

    public User createUser(String username, String password, String fullName, UserRole role, String farmerId) {
        ValidationUtil.require(!ValidationUtil.isBlank(username), "Username is required.");
        ValidationUtil.require(!ValidationUtil.isBlank(password), "Password is required.");
        ValidationUtil.require(userDAO.findAll().stream().noneMatch(u -> u.getUsername().equalsIgnoreCase(username)),
                "Username already exists.");

        User user;
        String id = IdGenerator.generate("USR");
        if (role == UserRole.ADMIN) {
            user = new AdminUser(id, username, password, fullName);
        } else if (role == UserRole.OFFICER) {
            user = new CooperativeOfficerUser(id, username, password, fullName);
        } else {
            user = new FarmerUser(id, username, password, fullName, farmerId);
        }
        userDAO.save(user);
        return user;
    }

    private void seedDefaultUsers() {
        if (!userDAO.findAll().isEmpty()) {
            return;
        }
        userDAO.save(new AdminUser(IdGenerator.generate("USR"), "admin", "admin123", "System Admin"));
        userDAO.save(new CooperativeOfficerUser(IdGenerator.generate("USR"), "officer1", "officer123", "Coop Officer"));
    }
}

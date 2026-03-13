package com.agrinepal.dao;

import com.agrinepal.model.User;

public class UserDAO extends FileBasedDAO<User> {
    public UserDAO() {
        super("data/users.dat");
    }
}

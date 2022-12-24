package org.devrisby.c195.services;

import org.devrisby.c195.data.UserRepository;
import org.devrisby.c195.models.User;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

public class UserService {

    private UserRepository userDAO;

    public UserService(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<User> loginUser(String userName, String password) {
        Optional<User> user = userDAO.findByUserName(userName);

        if(user.isPresent()) {
            if(user.get().getPassword().equals(password)) {
                return user;
            };
        }

        return Optional.empty();
    }
}

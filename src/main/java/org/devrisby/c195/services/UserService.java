package org.devrisby.c195.services;

import org.devrisby.c195.data.DB;
import org.devrisby.c195.data.UserRepository;
import org.devrisby.c195.models.User;

import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository(DB.getConnection());
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public Optional<User> loginUser(String userName, String password) {
        Optional<User> user = this.userRepository.findByUserName(userName);

        if(user.isPresent()) {
            if(user.get().getPassword().equals(password)) {
                return user;
            };
        }

        return Optional.empty();
    }
}

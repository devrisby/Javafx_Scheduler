package org.devrisby.c195.modules.user;

import org.devrisby.c195.data.AppDataSource;

import java.util.List;
import java.util.Optional;

/** Class for User business logic */
public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository(AppDataSource.getConnection());
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    /** Validates login information and returns matching user.
     *
     * Retrieves user from database and compares password for validation
     * @param userName - username for User
     * @param password - password for User */
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

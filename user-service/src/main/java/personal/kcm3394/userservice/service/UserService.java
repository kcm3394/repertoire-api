package personal.kcm3394.userservice.service;

import personal.kcm3394.userservice.model.User;

import java.util.Optional;

public interface UserService {
    
    User findUserByUsername(String username);
    
    User saveUser(User user);
    
    Optional<User> findUserById(Long id);

    void deleteUser(Long id);
}

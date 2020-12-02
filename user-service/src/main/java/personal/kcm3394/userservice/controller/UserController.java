package personal.kcm3394.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.userservice.model.CreateUserRequest;
import personal.kcm3394.userservice.model.UpdateUserRequest;
import personal.kcm3394.userservice.model.User;
import personal.kcm3394.userservice.model.dtos.UserDto;
import personal.kcm3394.userservice.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isEmpty()) {
            //todo custom error response
            log.error("User by id " + id + " not found");
            return ResponseEntity.notFound().build();
        }
        log.info("Finding user with id: " + id);
        return ResponseEntity.ok(convertEntityToUserDto(user.get()));
    }

    @GetMapping
    public ResponseEntity<UserDto> findUserByUsername(@RequestParam String username) {
        //todo this should only be allowed by the user of the same id OR user with admin privileges
        User user = userService.findUserByUsername(username);
        if (user == null) {
            //todo custom error response
            log.error("User by username " + username + " not found");
            return ResponseEntity.notFound().build();
        }
        log.info("Finder user with username: " + username);
        return ResponseEntity.ok(convertEntityToUserDto(user));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();

        if (userService.findUserByUsername(createUserRequest.getUsername()) != null) {
            //todo custom error response
            log.error("Username already exists");
            return ResponseEntity.badRequest().build();
        }
        user.setUsername(createUserRequest.getUsername());

        if (createUserRequest.getPassword().length() < 8 ||
                !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            log.error("Password length does not meet requirements and/or passwords do not match");
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        user.setFach(createUserRequest.getFach());

        log.info("Saving user: " + createUserRequest.getUsername());
        return ResponseEntity.ok(convertEntityToUserDto(userService.saveUser(user)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        //todo this should only be allowed by the user of the same id OR user with admin privileges
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isEmpty()) {
            //todo custom error response
            log.error("User by id " + id + " not found");
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();

        if (userService.findUserByUsername(updateUserRequest.getUsername()) != null) {
            //todo custom error response
            log.error("Username already exists");
            return ResponseEntity.badRequest().build();
        }
        user.setUsername(updateUserRequest.getUsername());
        user.setFach(updateUserRequest.getFach());

        log.info("Updating user " + id + " to " + updateUserRequest.getUsername() + " and " + updateUserRequest.getFach().toString());
        return ResponseEntity.ok(convertEntityToUserDto(userService.saveUser(user)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        //todo this should only be allowed by the user of the same id OR user with admin privileges
        Optional<User> user = userService.findUserById(id);
        if (user.isEmpty()) {
            //todo custom error response
            log.error("User by id " + id + " not found");
            return ResponseEntity.notFound().build();
        }
        //todo when delete user should also delete associated repertoire in other service
        log.info("Deleting user with id: " + id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    private static UserDto convertEntityToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}

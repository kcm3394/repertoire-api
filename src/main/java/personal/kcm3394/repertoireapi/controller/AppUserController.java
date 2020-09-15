package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.CreateUserRequest;
import personal.kcm3394.repertoireapi.domain.dtos.AppUserDTO;
import personal.kcm3394.repertoireapi.exceptions.NoEntityFoundException;
import personal.kcm3394.repertoireapi.exceptions.UserCreationException;
import personal.kcm3394.repertoireapi.service.AppUserService;

import java.util.Optional;

/**
 * Handles user requests for creating a new user and ensuring password requirements are met.
 * Ensures password is securely stored in the database by using BCryptPasswordEncoder.
 */
@RestController //@Controller + @ResponseBody
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUserController(AppUserService appUserService,
                             BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> createUser(@RequestBody CreateUserRequest createUserRequest) {
        AppUser appUser = new AppUser();

        if(appUserService.findUserByUsername(createUserRequest.getUsername()) != null) {
            throw new UserCreationException("Username already exists");
        }
        appUser.setUsername(createUserRequest.getUsername());

        if(createUserRequest.getPassword().length() < 8 ||
                !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            throw new UserCreationException("Password must be at least 8 characters long and passwords must match");
        }
        appUser.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        appUser.setFach(createUserRequest.getFach());

        appUserService.saveUser(appUser);
        AppUserDTO appUserDTO = covertEntityToAppUserDTO(appUser);
        return ResponseEntity.ok(appUserDTO);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AppUserDTO> findUserById(@PathVariable Long id) {
        Optional<AppUser> appUser = appUserService.findUserById(id);
        if (appUser.isEmpty()) {
            throw new NoEntityFoundException("User " + id + " not found");
        }
        return ResponseEntity.ok(covertEntityToAppUserDTO(appUser.get()));
    }

    private static AppUserDTO covertEntityToAppUserDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        return appUserDTO;
    }
}

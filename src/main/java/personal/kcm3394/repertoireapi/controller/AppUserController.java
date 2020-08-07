package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.AppUserDTO;
import personal.kcm3394.repertoireapi.domain.CreateUserRequest;
import personal.kcm3394.repertoireapi.service.AppUserService;

@RestController //@Controller + @ResponseBody
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUserController(AppUserService appUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> createUser(@RequestBody CreateUserRequest createUserRequest) {
        AppUser appUser = new AppUser();

        if(appUserService.findUserByUsername(createUserRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().build();
        }
        appUser.setUsername(createUserRequest.getUsername());

        if(createUserRequest.getPassword().length() < 8 ||
                !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().build();
        }
        appUser.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        appUser.setFach(createUserRequest.getFach());

        appUserService.saveUser(appUser);
        AppUserDTO appUserDTO = covertEntityToAppUserDTO(appUser);
        return ResponseEntity.ok(appUserDTO);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AppUserDTO> findUserById(@PathVariable Long id) {
        AppUser appUser = appUserService.findUserById(id);
        return appUser != null ? ResponseEntity.ok(covertEntityToAppUserDTO(appUser)) : ResponseEntity.notFound().build();
    }

    private static AppUserDTO covertEntityToAppUserDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        return appUserDTO;
    }
}

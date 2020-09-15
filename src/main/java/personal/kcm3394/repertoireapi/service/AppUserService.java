package personal.kcm3394.repertoireapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.repository.AppUserRepository;

import java.util.Optional;

/**
 * Makes calls to the database layer related to CRUD operations for AppUsers
 */
@Service
@Transactional
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser findUserByUsername(String username) {
        return appUserRepository.findAppUserByUsername(username);
    }

    public AppUser saveUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public Optional<AppUser> findUserById(Long id) {
        return appUserRepository.findById(id);
    }
}

//https://stackoverflow.com/questions/24482117/when-use-getone-and-findone-methods-spring-data-jpa/31074552
//"Since there is some unpredictable situations with getOne(), it is recommended to use findById() instead."

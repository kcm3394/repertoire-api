package personal.kcm3394.repertoireapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.repository.AppUserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void should_return_user_by_username() {
        AppUser user = new AppUser();
        user.setUsername("test");
        when(appUserRepository.findAppUserByUsername(anyString())).thenReturn(user);

        AppUser found = appUserService.findUserByUsername("test");
        assertEquals("test", found.getUsername());
    }

    @Test
    void when_save_user_should_return_saved_user() {
        AppUser user = new AppUser();
        user.setUsername("test");
        user.setPassword("password");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(user);

        AppUser saved = appUserService.saveUser(user);
        assertEquals("test", saved.getUsername());
    }

    @Test
    void should_return_user_by_id() {
        AppUser user = new AppUser();
        user.setId(1L);
        when(appUserRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertTrue(appUserService.findUserById(1L).isPresent());
        AppUser found = appUserService.findUserById(1L).get();
        assertEquals(1L, found.getId());
    }
}

package personal.kcm3394.repertoireapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.enums.Fach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("thisIsEncoded");
        user.setFach(Fach.SOPRANO);
        testEntityManager.persistAndFlush(user);
    }

    @Test
    void should_save_user_and_give_id() {
        AppUser appUser = new AppUser();
        appUser.setUsername("testing");
        appUser.setPassword("EncodedPwd");
        AppUser saved = appUserRepository.save(appUser);

        assertTrue(appUserRepository.findById(saved.getId()).isPresent());
        assertThat(appUserRepository.findById(saved.getId()).get(), equalTo(appUser));
        assertThat(appUserRepository.findById(saved.getId()).get().getId(), isA(Long.class));
    }

    @Test
    void should_return_user_by_username() {
        AppUser found = appUserRepository.findAppUserByUsername("testUser");

        assertNotNull(found);
        assertThat(found.getUsername(), is("testUser"));
        assertThat(found.getFach(), is(Fach.SOPRANO));
        assertThat(found.getId(), isA(Long.class));
    }

    @Test
    void should_return_user_by_id() {
        Optional<AppUser> optionalFound = appUserRepository.findById(user.getId());

        assertTrue(optionalFound.isPresent());
        assertThat(optionalFound.get().getId(), isA(Long.class));
        assertThat(optionalFound.get().getUsername(), is("testUser"));
    }

    @Test
    void should_update_user() {
        Optional<AppUser> optionalFound = appUserRepository.findById(user.getId());
        assertTrue(optionalFound.isPresent());

        AppUser toChange = optionalFound.get();
        toChange.setUsername("different");
        toChange.setPassword("different");
        toChange.setFach(Fach.MEZZO_SOPRANO);

        AppUser changed = appUserRepository.save(toChange);

        assertThat(changed.getId(), is(user.getId()));
        assertThat(changed.getUsername(), is("different"));
        assertThat(changed.getPassword(), is("different"));
        assertThat(changed.getFach(), is(Fach.MEZZO_SOPRANO));
    }
}

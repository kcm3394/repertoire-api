package personal.kcm3394.userservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import personal.kcm3394.userservice.model.Fach;
import personal.kcm3394.userservice.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testUser")
                .password("thisIsEncoded")
                .fach(Fach.SOPRANO)
                .build();
        testEntityManager.persistAndFlush(user);
    }

    @Test
    void should_save_user_and_give_id() {
        User newUser = User.builder()
                .username("testing")
                .password("EncodedPwd")
                .build();

        User saved = userRepository.save(newUser);

        assertTrue(userRepository.findById(saved.getId()).isPresent());
        assertThat(userRepository.findById(saved.getId()).get(), equalTo(newUser));
        assertThat(userRepository.findById(saved.getId()).get().getId(), isA(Long.class));
    }

    @Test
    void should_return_user_by_username() {
        User found = userRepository.findUserByUsername("testUser");

        assertNotNull(found);
        assertThat(found.getUsername(), is("testUser"));
        assertThat(found.getFach(), is(Fach.SOPRANO));
        assertThat(found.getId(), isA(Long.class));
    }

    @Test
    void should_return_user_by_id() {
        Optional<User> optionalFound = userRepository.findById(user.getId());

        assertTrue(optionalFound.isPresent());
        assertThat(optionalFound.get().getId(), isA(Long.class));
        assertThat(optionalFound.get().getUsername(), is("testUser"));
    }

    @Test
    void should_update_user() {
        Optional<User> optionalFound = userRepository.findById(user.getId());
        assertTrue(optionalFound.isPresent());

        User toChange = optionalFound.get();
        toChange.setUsername("different");
        toChange.setPassword("different");
        toChange.setFach(Fach.MEZZO_SOPRANO);

        User changed = userRepository.save(toChange);

        assertThat(changed.getId(), is(user.getId()));
        assertThat(changed.getUsername(), is("different"));
        assertThat(changed.getPassword(), is("different"));
        assertThat(changed.getFach(), is(Fach.MEZZO_SOPRANO));
    }

    @Test
    void should_delete_user() {
        userRepository.deleteById(user.getId());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
}

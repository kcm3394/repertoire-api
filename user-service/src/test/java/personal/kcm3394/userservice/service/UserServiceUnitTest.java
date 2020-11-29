package personal.kcm3394.userservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import personal.kcm3394.userservice.model.User;
import personal.kcm3394.userservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void should_return_user_by_username() {
        User user = User.builder()
                .username("test")
                .build();
        when(userRepository.findUserByUsername(anyString())).thenReturn(user);

        User found = userService.findUserByUsername("test");
        assertEquals("test", found.getUsername());
    }

    @Test
    void when_save_user_should_return_saved_user() {
        User user = User.builder()
                .username("test")
                .password("password")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.saveUser(user);
        assertEquals("test", saved.getUsername());
    }

    @Test
    void should_return_user_by_id() {
        User user = User.builder()
                .id(1L)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertTrue(userService.findUserById(1L).isPresent());
        User found = userService.findUserById(1L).get();
        assertEquals(1L, found.getId());
    }

    @Test
    void verify_delete_method_called() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}

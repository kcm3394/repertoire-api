package personal.kcm3394.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import personal.kcm3394.userservice.model.CreateUserRequest;
import personal.kcm3394.userservice.model.Fach;
import personal.kcm3394.userservice.model.User;
import personal.kcm3394.userservice.service.UserService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void should_return_user_by_id() throws Exception {
        User user = buildUser();
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v2/user/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.fach").value("SOPRANO"));
    }

    @Test
    void should_return_404_when_user_id_does_not_exist() throws Exception {
        when(userService.findUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/user/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_user_by_username() throws Exception {
        User user = buildUser();
        when(userService.findUserByUsername("testUser")).thenReturn(user);

        mockMvc.perform(get("/api/v2/user")
                .param("username", "testUser")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.fach").value("SOPRANO"));
    }

    @Test
    void should_return_404_when_username_does_not_exist() throws Exception {
        when(userService.findUserByUsername("testUser")).thenReturn(null);

        mockMvc.perform(get("/api/v2/user")
                .param("username", "testUser")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_created_user() throws Exception {
        mockMvc.perform(post("/api/v2/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildUserRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.fach").value("SOPRANO"));
    }

    @Test
    void should_return_400_when_username_already_exists() throws Exception {
        User user = buildUser();
        when(userService.findUserByUsername("testUser")).thenReturn(user);

        mockMvc.perform(post("/api/v2/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildUserRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_password_doesnt_match() throws Exception {
        CreateUserRequest request = buildUserRequest();
        request.setConfirmPassword("testpassword");

        mockMvc.perform(post("/api/v2/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_password_length_req_not_met() throws Exception {
        CreateUserRequest request = buildUserRequest();
        request.setPassword("test");
        request.setConfirmPassword("test");

        mockMvc.perform(post("/api/v2/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_200_when_song_deleted() throws Exception {
        when(userService.findUserById(1L)).thenReturn(Optional.of(buildUser()));

        mockMvc.perform(delete("/api/v2/user/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private CreateUserRequest buildUserRequest() {
        return CreateUserRequest.builder()
                .fach(Fach.SOPRANO)
                .username("testUser")
                .password("testPassword")
                .confirmPassword("testPassword")
                .build();
    }

    private User buildUser() {
        return User.builder()
                .id(1L)
                .username("testUser")
                .password("thisIsEncoded")
                .fach(Fach.SOPRANO)
                .build();
    }
}

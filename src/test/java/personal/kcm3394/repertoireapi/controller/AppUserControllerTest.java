package personal.kcm3394.repertoireapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.CreateUserRequest;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.domain.enums.Fach;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Type;
import personal.kcm3394.repertoireapi.service.AppUserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//If you are using JUnit 5, there’s no need to add the equivalent @ExtendWith(SpringExtension.class)
//as @SpringBootTest and the other @…Test annotations are already annotated with it.
@SpringBootTest
@AutoConfigureMockMvc
public class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void shouldReturnCreatedUser() throws Exception {
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreateUserRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.fach").value("SOPRANO"));
    }

    @Test
    void shouldReturn400WhenUsernameAlreadyExists() throws Exception {
        AppUser appUser = getAppUser();
        when(appUserService.findUserByUsername("testUser")).thenReturn(appUser);

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCreateUserRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPasswordsDoNotMatch() throws Exception {
        CreateUserRequest request = getCreateUserRequest();
        request.setConfirmPassword("testpassword");

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPasswordLengthReqNotMet() throws Exception {
        CreateUserRequest request = getCreateUserRequest();
        request.setPassword("test");
        request.setConfirmPassword("test");

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnAppUserWithIdAndRepertoire() throws Exception {
        AppUser appUser = getAppUser();
        appUser.setRepertoire(getRepertoire());
        when(appUserService.findUserById(1L)).thenReturn(appUser);

        mockMvc.perform(
                get("/api/user/id/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.fach").value("SOPRANO"))
                .andExpect(jsonPath("$.repertoire[0].id").value(1))
                .andExpect(jsonPath("$.repertoire[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.repertoire[0].composer.name").value("Wolfgang Amadeus Mozart"));
    }

    @Test
    void shouldReturn401WhenAccessingAuthorizedUrl() throws Exception {
        mockMvc.perform(get("/api/user/id/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenUserIdDoesNotExist() throws Exception {
        when(appUserService.findUserById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/user/id/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private CreateUserRequest getCreateUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setFach(Fach.SOPRANO);
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        return request;
    }

    private AppUser getAppUser() {
        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("testUser");
        appUser.setPassword("thisIsEncoded");
        appUser.setFach(Fach.SOPRANO);

        return appUser;
    }

    private Set<Song> getRepertoire() {
        Composer mozart = new Composer();
        mozart.setId(1L);
        mozart.setName("Wolfgang Amadeus Mozart");
        mozart.setBirthDate(LocalDate.of(1756, 1, 1));
        mozart.setDeathDate(LocalDate.of(1791, 1, 1));
        mozart.setEpoch(Epoch.CLASSICAL);

        Song doveSono = new Song();
        doveSono.setId(1L);
        doveSono.setTitle("Dove sono i bei momenti");
        doveSono.setComposer(mozart);
        doveSono.setContainingWork("Le nozze di Figaro");
        doveSono.setDuration("5 minutes");
        doveSono.setLanguage(Language.ITALIAN);
        doveSono.setType(Type.ARIA);

        Set<Song> compositions = new HashSet<>();
        compositions.add(doveSono);
        mozart.setCompositions(compositions);

        return compositions;
    }

    private List<Song> getDisplayRepertoire() {
        Set<Song> repertoire = getRepertoire();
        return new ArrayList<>(repertoire);
    }
}

//https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-testing.html

package personal.kcm3394.repertoireapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.*;
import personal.kcm3394.repertoireapi.service.AppUserService;
import personal.kcm3394.repertoireapi.service.ComposerService;
import personal.kcm3394.repertoireapi.service.SongService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RepertoireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SongService songService;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private ComposerService composerService;

    @Test
    @WithMockUser
    void should_return_user_repertoire() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(songService.findAllSongsInUserRepertoire(any(), any(Pageable.class))).thenReturn(getDisplayRepertoire(pageable));

        mockMvc.perform(get("/api/repertoire")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[0].composer.name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_user_repertoire_by_status() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(songService.findAllSongsInUserRepertoireByStatus(anyLong(), any(Status.class), any(Pageable.class))).thenReturn(getDisplayRepertoire(pageable));

        mockMvc.perform(get("/api/repertoire/byStatus/PERFORMED")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[0].composer.name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_user_repertoire_by_language() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(songService.findAllSongsInUserRepertoireByLanguage(anyLong(), any(Language.class), any(Pageable.class))).thenReturn(getDisplayRepertoire(pageable));

        mockMvc.perform(get("/api/repertoire/byLanguage/ITALIAN")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[0].composer.name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_user_repertoire_by_composer() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(composerService.findComposerById(anyLong())).thenReturn(Optional.of(getMozart()));
        when(songService.findAllSongsInUserRepertoireByComposer(anyLong(), anyLong(), any(Pageable.class))).thenReturn(getDisplayRepertoire(pageable));

        mockMvc.perform(get("/api/repertoire/byComposer/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[0].composer.name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_not_found_when_composer_id_does_not_exist() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(composerService.findComposerById(anyLong())).thenReturn(Optional.empty());
        when(songService.findAllSongsInUserRepertoireByComposer(anyLong(), anyLong(), any(Pageable.class))).thenReturn(getDisplayRepertoire(pageable));

        mockMvc.perform(get("/api/repertoire/byComposer/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void should_return_user_repertoire_by_epoch() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(songService.findAllSongsInUserRepertoireByEpoch(anyLong(), any(Epoch.class), any(Pageable.class))).thenReturn(getDisplayRepertoire(pageable));

        mockMvc.perform(get("/api/repertoire/byEpoch/CLASSICAL")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[0].composer.name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_song_added_to_repertoire() throws Exception {
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(songService.findSongById(any())).thenReturn(Optional.of(getNessunDorma()));

        mockMvc.perform(post("/api/repertoire/add/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Nessun dorma"))
                .andExpect(jsonPath("$.composer.name").value("Giacomo Puccini"));
    }

    @Test
    @WithMockUser
    void should_return_404_when_song_does_not_exist() throws Exception {
        when(songService.findSongById(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/repertoire/add/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void should_return_400_when_deleting_song_not_in_rep() throws Exception {
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(songService.findSongById(any())).thenReturn(Optional.of(getNessunDorma()));

        mockMvc.perform(delete("/api/repertoire/delete/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_401_when_accessing_rep_without_auth() throws Exception {
        mockMvc.perform(get("/api/repertoire")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private AppUser getAppUser() {
        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("testUser");
        appUser.setPassword("thisIsEncoded");
        appUser.setFach(Fach.SOPRANO);
        appUser.setRepertoire(getRepertoire());

        return appUser;
    }

    private Set<Song> getRepertoire() {
        Set<Song> repertoire = new HashSet<>();
        repertoire.add(getDoveSono());

        return repertoire;
    }

    private Page<Song> getDisplayRepertoire(Pageable pageable) {
        Set<Song> repertoire = getRepertoire();
        return new PageImpl<>(new ArrayList<>(repertoire), pageable, repertoire.size());
    }

    private Song getDoveSono() {
        Composer mozart = new Composer();
        mozart.setId(1L);
        mozart.setName("Wolfgang Amadeus Mozart");

        Song doveSono = new Song();
        doveSono.setId(1L);
        doveSono.setTitle("Dove sono i bei momenti");
        doveSono.setComposer(mozart);
        doveSono.setContainingWork("Le nozze di Figaro");
        doveSono.setDuration("5 minutes");
        doveSono.setLanguage(Language.ITALIAN);
        doveSono.setType(Type.ARIA);

        return doveSono;
    }

    private Song getNessunDorma() {
        Composer puccini = new Composer();
        puccini.setId(2L);
        puccini.setName("Giacomo Puccini");

        Song nessun = new Song();
        nessun.setId(2L);
        nessun.setTitle("Nessun dorma");
        nessun.setComposer(puccini);
        nessun.setContainingWork("Turandot");
        nessun.setDuration("3 minutes");
        nessun.setLanguage(Language.ITALIAN);
        nessun.setType(Type.ARIA);

        return nessun;
    }

    private Composer getMozart() {
        Composer mozart = new Composer();
        mozart.setId(1L);
        mozart.setName("Wolfgang Amadeus Mozart");

        return mozart;
    }


}

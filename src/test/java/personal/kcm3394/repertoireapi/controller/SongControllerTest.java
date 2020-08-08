package personal.kcm3394.repertoireapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Type;
import personal.kcm3394.repertoireapi.service.ComposerService;
import personal.kcm3394.repertoireapi.service.SongService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    @MockBean
    private ComposerService composerService;

    @Test
    @WithMockUser
    void shouldReturnListOfSongs() throws Exception {
        when(songService.getAllSongs()).thenReturn(getAllSongs());

        mockMvc.perform(get("/api/song")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.[1].title").value("Nessun dorma"));
    }

    @Test
    @WithMockUser
    void shouldReturnCreatedSong() throws Exception {
        when(songService.saveSong(any(Song.class))).thenReturn(getDoveSono());
        when(composerService.findComposerById(1L)).thenReturn(getMozart());

        mockMvc.perform(post("/api/song/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getDoveSono()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.composer.name").value("Wolfgang Amadeus Mozart"));
    }

    @Test
    @WithMockUser
    void shouldReturn200WhenSongDeleted() throws Exception {
        when(songService.findSongById(1L)).thenReturn(getDoveSono());

        mockMvc.perform(delete("/api/song/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401WhenAccessingSongsWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/song")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenSongIdDoesNotExist() throws Exception {
        when(songService.findSongById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/song/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenComposerNotYetCreated() throws Exception {
        when(composerService.findComposerById(1L)).thenReturn(null);

        mockMvc.perform(post("/api/song/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getDoveSono()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private Composer getMozart() {
        Composer mozart = new Composer();
        mozart.setId(1L);
        mozart.setName("Wolfgang Amadeus Mozart");

        return mozart;
    }

    private Song getDoveSono() {
        Composer mozart = getMozart();

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

    private List<Song> getAllSongs() {
        Song doveSono = getDoveSono();
        Song nessun = getNessunDorma();
        List<Song> songs = new ArrayList<>();
        songs.add(doveSono);
        songs.add(nessun);

        return songs;
    }
}

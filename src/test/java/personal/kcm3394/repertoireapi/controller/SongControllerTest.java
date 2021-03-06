package personal.kcm3394.repertoireapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Type;
import personal.kcm3394.repertoireapi.service.ComposerService;
import personal.kcm3394.repertoireapi.service.SongService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    void should_return_list_of_songs() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(songService.getAllSongs(any(Pageable.class))).thenReturn(getAllSongs(pageable));

        mockMvc.perform(get("/api/song")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[1].title").value("Nessun dorma"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_created_song() throws Exception {
        when(songService.saveSong(any(Song.class))).thenReturn(getDoveSono());
        when(composerService.findComposerById(1L)).thenReturn(Optional.of(getMozart()));

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
    void should_return_200_when_song_deleted() throws Exception {
        when(songService.findSongById(1L)).thenReturn(Optional.of(getDoveSono()));

        mockMvc.perform(delete("/api/song/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_401_when_accessing_songs_without_auth() throws Exception {
        mockMvc.perform(get("/api/song")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void should_return_404_when_song_id_does_not_exist() throws Exception {
        when(songService.findSongById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/song/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void should_return_404_when_composer_not_yet_created() throws Exception {
        when(composerService.findComposerById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/song/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getDoveSono()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void should_return_list_with_dove_sono_when_search_by_title() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Song> justDoveSono = new ArrayList<>();
        justDoveSono.add(getDoveSono());
        PageImpl<Song> justDovePage = new PageImpl<>(justDoveSono, pageable, justDoveSono.size());
        when(songService.searchSongsByTitle(anyString(), any(Pageable.class))).thenReturn(justDovePage);

        mockMvc.perform(get("/api/song/search/title/sono")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_list_with_dove_sono_when_search_by_composer() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Song> justDoveSono = new ArrayList<>();
        justDoveSono.add(getDoveSono());
        PageImpl<Song> justDovePage = new PageImpl<>(justDoveSono, pageable, justDoveSono.size());
        when(songService.searchSongsByComposer(anyString(), any(Pageable.class))).thenReturn(justDovePage);

        mockMvc.perform(get("/api/song/search/composer/mozart")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
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

    private Page<Song> getAllSongs(Pageable pageable) {
        Song doveSono = getDoveSono();
        Song nessun = getNessunDorma();
        List<Song> songs = new ArrayList<>();
        songs.add(doveSono);
        songs.add(nessun);

        return new PageImpl<>(songs, pageable, songs.size());
    }
}

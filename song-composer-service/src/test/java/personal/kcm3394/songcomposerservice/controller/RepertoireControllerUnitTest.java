package personal.kcm3394.songcomposerservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import personal.kcm3394.songcomposerservice.model.*;
import personal.kcm3394.songcomposerservice.service.ComposerService;
import personal.kcm3394.songcomposerservice.service.RepertoireService;
import personal.kcm3394.songcomposerservice.service.SongService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepertoireController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RepertoireControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepertoireService repertoireService;

    @MockBean
    private SongService songService;

    @MockBean
    private ComposerService composerService;

    @Test
    void should_return_repertoire_by_id() throws Exception {
        when(repertoireService.findRepertoireById(1L)).thenReturn(Optional.of(buildRepertoire()));

        mockMvc.perform(get("/api/v2/repertoire/repId/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.repertoire").isNotEmpty());
    }

    @Test
    void should_return_404_when_repertoire_id_doesnt_exist() throws Exception {
        when(repertoireService.findRepertoireById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/repertoire/repId/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_200_when_creating_repertoire_for_new_user() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(null);

        mockMvc.perform(put("/api/v2/repertoire/2/create-repertoire")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_400_when_creating_repertoire_for_existing_user() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());

        mockMvc.perform(put("/api/v2/repertoire/2/create-repertoire")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_delete_repertoire_for_deleted_user() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());

        mockMvc.perform(delete("/api/v2/repertoire/2/delete-repertoire")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_400_when_deleting_repertoire_for_user_without_repertoire() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(null);

        mockMvc.perform(delete("/api/v2/repertoire/2/delete-repertoire")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_repertoire_by_user_id() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());
        when(songService.findAllSongsInRepertoire(anyLong(), any(Pageable.class))).thenReturn(getAllSongs());

        mockMvc.perform(get("/api/v2/repertoire/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[1].title").value("Nessun dorma"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    void should_return_songs_in_repertoire_only_in_certain_language() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());
        when(songService.findAllSongsInRepertoireByLanguage(anyLong(), any(Language.class), any(Pageable.class))).thenReturn(getAllSongs());

        mockMvc.perform(get("/api/v2/repertoire/2/language/italian")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[1].title").value("Nessun dorma"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    void should_return_songs_in_repertoire_only_by_certain_composer() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());

        ArrayList<Song> justDoveSono = new ArrayList<>();
        justDoveSono.add(buildDoveSono());
        PageImpl<Song> justDovePage = new PageImpl<>(justDoveSono, PageRequest.of(0, 5), justDoveSono.size());
        when(songService.findAllSongsInRepertoireByComposer(anyLong(), anyLong(), any(Pageable.class))).thenReturn(justDovePage);

        when(composerService.findComposerById(anyLong())).thenReturn(Optional.of(buildMozart()));

        mockMvc.perform(get("/api/v2/repertoire/2/composer/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    void should_return_songs_in_repertoire_only_in_certain_epoch() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());

        ArrayList<Song> justDoveSono = new ArrayList<>();
        justDoveSono.add(buildDoveSono());
        PageImpl<Song> justDovePage = new PageImpl<>(justDoveSono, PageRequest.of(0, 5), justDoveSono.size());
        when(songService.findAllSongsInRepertoireByEpoch(anyLong(), any(Epoch.class), any(Pageable.class))).thenReturn(justDovePage);

        mockMvc.perform(get("/api/v2/repertoire/2/epoch/classical")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    void should_add_song_to_repertoire() throws Exception {
        Song newSong = Song.builder()
                .id(3L)
                .title("Porgi amor")
                .composer(buildMozart())
                .build();
        when(songService.findSongById(anyLong())).thenReturn(Optional.of(newSong));
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());

        mockMvc.perform(post("/api/v2/repertoire/2/add-song/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Porgi amor"))
                .andExpect(jsonPath("$.composer.name").value("Wolfgang Amadeus Mozart"));
    }

    @Test
    void should_return_404_when_adding_song_that_doesnt_exist() throws Exception {
        when(songService.findSongById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v2/repertoire/2/add-song/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_404_when_user_doesnt_have_repertoire() throws Exception {
        when(songService.findSongById(anyLong())).thenReturn(Optional.of(buildDoveSono()));
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(null);

        mockMvc.perform(post("/api/v2/repertoire/2/add-song/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //TODO test for equals method so can check if repertoire has song or not, then test

//    @Test
//    void should_return_400_when_adding_song_already_in_repertoire() throws Exception {
//        when(songService.findSongById(anyLong())).thenReturn(Optional.of(buildDoveSono()));
//        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());
//
//        mockMvc.perform(post("/api/v2/repertoire/2/add-song/1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    void should_delete_song_from_repertoire() throws Exception {
//        when(songService.findSongById(anyLong())).thenReturn(Optional.of(buildDoveSono()));
//        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());
//
//        mockMvc.perform(delete("/api/v2/repertoire/2/delete-song/1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

    @Test
    void should_return_400_when_deleting_song_not_in_repertoire() throws Exception {
        Song newSong = Song.builder()
                .id(3L)
                .title("Porgi amor")
                .composer(buildMozart())
                .build();
        when(songService.findSongById(anyLong())).thenReturn(Optional.of(newSong));
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());

        mockMvc.perform(delete("/api/v2/repertoire/2/delete-song/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_clear_repertoire() throws Exception {
        when(repertoireService.findRepertoireByUserId(2L)).thenReturn(buildRepertoire());

        mockMvc.perform(put("/api/v2/repertoire/2/reset")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private Composer buildMozart() {
        return Composer.builder()
                .id(1L)
                .name("Wolfgang Amadeus Mozart")
                .epoch(Epoch.CLASSICAL)
                .build();
    }

    private Repertoire buildRepertoire() {
        Song s1 = buildDoveSono();
        Song s2 = buildNessunDorma();

        Set<Song> songs = new HashSet<>();
        songs.add(s1);
        songs.add(s2);

        return Repertoire.builder()
                .id(1L)
                .userId(1L)
                .repertoire(songs)
                .build();
    }

    private Song buildDoveSono() {
        return Song.builder()
                .id(1L)
                .title("Dove sono i bei momenti")
                .composer(buildMozart())
                .language(Language.ITALIAN)
                .build();
    }

    private Song buildNessunDorma() {
        Composer puccini = Composer.builder()
                .id(2L)
                .name("Giacomo Puccini")
                .epoch(Epoch.LATE_ROMANTIC)
                .build();

        return Song.builder()
                .id(2L)
                .title("Nessun dorma")
                .composer(puccini)
                .language(Language.ITALIAN)
                .build();
    }

    private Page<Song> getAllSongs() {
        Song doveSono = buildDoveSono();
        Song nessun = buildNessunDorma();
        List<Song> songs = new ArrayList<>();
        songs.add(doveSono);
        songs.add(nessun);

        return new PageImpl<>(songs, PageRequest.of(0, 5), songs.size());
    }
}

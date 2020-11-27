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
import personal.kcm3394.songcomposerservice.domain.Composer;
import personal.kcm3394.songcomposerservice.domain.Language;
import personal.kcm3394.songcomposerservice.domain.Song;
import personal.kcm3394.songcomposerservice.domain.Type;
import personal.kcm3394.songcomposerservice.service.SongService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SongControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SongService songService;

    @Test
    void should_return_list_of_songs() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(songService.getAllSongs(any(Pageable.class))).thenReturn(getAllSongs(pageable));

        mockMvc.perform(get("/api/v2/songs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.content[1].title").value("Nessun dorma"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    //todo implement test for addOrUpdateSong method

    @Test
    void should_return_200_when_song_deleted() throws Exception {
        when(songService.findSongById(1L)).thenReturn(Optional.of(buildDoveSono()));

        mockMvc.perform(delete("/api/v2/songs/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_404_when_song_id_does_not_exist() throws Exception {
        when(songService.findSongById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v2/songs/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_list_with_dove_sono_when_search_by_title() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Song> justDoveSono = new ArrayList<>();
        justDoveSono.add(buildDoveSono());
        PageImpl<Song> justDovePage = new PageImpl<>(justDoveSono, pageable, justDoveSono.size());
        when(songService.searchSongsByTitle(anyString(), any(Pageable.class))).thenReturn(justDovePage);

        mockMvc.perform(get("/api/v2/songs/search/title/sono")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    void should_return_list_with_dove_sono_when_search_by_composer() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Song> justDoveSono = new ArrayList<>();
        justDoveSono.add(buildDoveSono());
        PageImpl<Song> justDovePage = new PageImpl<>(justDoveSono, pageable, justDoveSono.size());
        when(songService.searchSongsByComposer(anyString(), any(Pageable.class))).thenReturn(justDovePage);

        mockMvc.perform(get("/api/v2/songs/search/composer/mozart")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    private Composer buildMozart() {
        return Composer.builder()
                .id(1L)
                .name("Wolfgang Amadeus Mozart")
                .build();
    }

    private Song buildDoveSono() {
        return Song.builder()
                .id(1L)
                .title("Dove sono i bei momenti")
                .composer(buildMozart())
                .containingWork("Le nozze di Figaro")
                .duration("5 minutes")
                .language(Language.ITALIAN)
                .type(Type.ARIA)
                .build();
    }

    private Song buildNessunDorma() {
        Composer puccini = Composer.builder()
                .id(2L)
                .name("Giacomo Puccini")
                .build();

        return Song.builder()
                .id(2L)
                .title("Nessun dorma")
                .composer(puccini)
                .containingWork("Turandot")
                .duration("3 minutes")
                .language(Language.ITALIAN)
                .type(Type.ARIA)
                .build();
    }

    private Page<Song> getAllSongs(Pageable pageable) {
        Song doveSono = buildDoveSono();
        Song nessun = buildNessunDorma();
        List<Song> songs = new ArrayList<>();
        songs.add(doveSono);
        songs.add(nessun);

        return new PageImpl<>(songs, pageable, songs.size());
    }
}

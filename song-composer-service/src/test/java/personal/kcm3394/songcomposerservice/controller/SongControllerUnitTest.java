package personal.kcm3394.songcomposerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import personal.kcm3394.songcomposerservice.model.Composer;
import personal.kcm3394.songcomposerservice.model.Language;
import personal.kcm3394.songcomposerservice.model.Song;
import personal.kcm3394.songcomposerservice.model.Type;
import personal.kcm3394.songcomposerservice.model.dtos.ComposerDto;
import personal.kcm3394.songcomposerservice.model.dtos.SongDto;
import personal.kcm3394.songcomposerservice.service.ComposerService;
import personal.kcm3394.songcomposerservice.service.SongService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SongControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    @MockBean
    private ComposerService composerService;

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

    @Test
    void should_return_created_song() throws Exception {
        when(songService.saveSong(any(Song.class))).thenReturn(buildDoveSono());
        when(composerService.findComposerById(1L)).thenReturn(Optional.of(buildMozart()));

        mockMvc.perform(post("/api/v2/songs/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDoveSono()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.composer.name").value("Wolfgang Amadeus Mozart"));
    }

    @Test
    void should_return_400_when_adding_song_without_composer() throws Exception {
        SongDto dto = new SongDto();
        dto.setTitle("Song");
        when(composerService.findComposerById(any())).thenReturn(Optional.empty());
        when(songService.findSongByTitleAndComposer(anyString(), anyLong())).thenReturn(null);

        mockMvc.perform(post("/api/v2/songs/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_adding_or_updating_song_without_composer_id() throws Exception {
        SongDto dto = new SongDto();
        dto.setTitle("Song");
        ComposerDto composerDto = new ComposerDto();
        dto.setComposer(composerDto);
        when(composerService.findComposerById(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v2/songs/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_adding_same_song() throws Exception {
        when(songService.findSongByTitleAndComposer(anyString(), anyLong())).thenReturn(buildDoveSono());
        when(composerService.findComposerById(1L)).thenReturn(Optional.of(buildMozart()));

        mockMvc.perform(post("/api/v2/songs/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDoveSono()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_updated_song() throws Exception {
        Song updated = buildDoveSono();
        updated.setLanguage(Language.ENGLISH);
        when(songService.findSongById(anyLong())).thenReturn(Optional.of(buildDoveSono()));
        when(composerService.findComposerById(1L)).thenReturn(Optional.of(buildMozart()));
        when(songService.saveSong(any(Song.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v2/songs/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.language").value("ENGLISH"));
    }

    @Test
    void should_return_400_when_updating_song_without_song_id() throws Exception {
        SongDto dto = new SongDto();
        dto.setTitle("Song");

        mockMvc.perform(put("/api/v2/songs/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_updating_song_without_composer_id() throws Exception {
        SongDto dto = new SongDto();
        dto.setId(1L);
        dto.setTitle("Song");
        ComposerDto composerDto = new ComposerDto();
        dto.setComposer(composerDto);
        when(composerService.findComposerById(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v2/songs/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_404_when_updating_song_with_nonexisting_composer_id() throws Exception {
        when(songService.findSongById(anyLong())).thenReturn(Optional.of(buildDoveSono()));
        when(composerService.findComposerById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v2/songs/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDoveSono()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_404_when_updating_song_with_nonexisting_song_id() throws Exception {
        when(songService.findSongById(anyLong())).thenReturn(Optional.empty());
        when(composerService.findComposerById(anyLong())).thenReturn(Optional.of(buildMozart()));

        mockMvc.perform(put("/api/v2/songs/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDoveSono()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

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

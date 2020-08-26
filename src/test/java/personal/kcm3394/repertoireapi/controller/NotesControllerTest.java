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
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Notes;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Fach;
import personal.kcm3394.repertoireapi.domain.enums.Status;
import personal.kcm3394.repertoireapi.service.AppUserService;
import personal.kcm3394.repertoireapi.service.NotesService;
import personal.kcm3394.repertoireapi.service.SongService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private NotesService notesService;

    @Test
    @WithMockUser
    void shouldReturnNotesForSong() throws Exception {
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(notesService.getNotesBySongIdAndUserId(anyLong(), anyLong())).thenReturn(getNotes());

        mockMvc.perform(get("/api/notes/song/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songTitle").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.status").value("PERFORMED"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenSongHasNoNote() throws Exception {
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(notesService.getNotesBySongIdAndUserId(anyLong(), anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/notes/song/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturnAddedNotesToSong() throws Exception {
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(notesService.getNotesBySongIdAndUserId(anyLong(), anyLong())).thenReturn(null);
        when(appUserService.findUserById(anyLong())).thenReturn(getAppUser());
        when(songService.findSongById(anyLong())).thenReturn(getDoveSono());
        when(notesService.saveNote(any(Notes.class))).thenReturn(getNotes());

        mockMvc.perform(post("/api/notes/add/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNotes()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songTitle").value("Dove sono i bei momenti"))
                .andExpect(jsonPath("$.status").value("PERFORMED"));
    }

    @Test
    @WithMockUser
    void shouldReturn200WhenNoteDeleted() throws Exception {
        when(appUserService.findUserByUsername(any())).thenReturn(getAppUser());
        when(notesService.getNotesBySongIdAndUserId(anyLong(), anyLong())).thenReturn(getNotes());

        mockMvc.perform(delete("/api/notes/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401WhenAccessingNotesWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/notes/song/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private AppUser getAppUser() {
        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("testUser");
        appUser.setPassword("thisIsEncoded");
        appUser.setFach(Fach.SOPRANO);

        return appUser;
    }

    private Notes getNotes() {
        Notes notes = new Notes();
        notes.setId(1L);
        notes.setStatus(Status.PERFORMED);
        notes.setMediaLink("https://www.youtube.com/watch?v=ucXGftnIxHM");
        notes.setNotes("Need to remember core support for legato but quiet line");
        notes.setSong(getDoveSono());
        notes.setUser(getAppUser());

        return notes;
    }

    private Song getDoveSono() {
        Composer mozart = new Composer();
        mozart.setId(1L);
        mozart.setName("Wolfgang Amadeus Mozart");

        Song doveSono = new Song();
        doveSono.setId(1L);
        doveSono.setTitle("Dove sono i bei momenti");
        doveSono.setComposer(mozart);

        return doveSono;
    }
}

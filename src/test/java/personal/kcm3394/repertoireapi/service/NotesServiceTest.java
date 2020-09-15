package personal.kcm3394.repertoireapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Notes;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Fach;
import personal.kcm3394.repertoireapi.domain.enums.Status;
import personal.kcm3394.repertoireapi.repository.NotesRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class NotesServiceTest {

    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NotesService notesService;

    @Test
    void should_return_saved_note() {
        when(notesRepository.save(any(Notes.class))).thenReturn(getNotes());

        Notes saved = notesService.saveNote(getNotes());
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("Dove sono i bei momenti", saved.getSong().getTitle());
    }

    @Test
    void verify_delete_method_called() {
        notesService.deleteNotes(1L);
        verify(notesRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void should_return_notes_for_song() {
        when(notesRepository.findByUser_IdAndSong_Id(anyLong(), anyLong())).thenReturn(getNotes());

        Notes found = notesService.getNotesBySongIdAndUserId(1L, 1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Dove sono i bei momenti", found.getSong().getTitle());
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

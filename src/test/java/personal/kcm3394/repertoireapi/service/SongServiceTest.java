package personal.kcm3394.repertoireapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Status;
import personal.kcm3394.repertoireapi.domain.enums.Type;
import personal.kcm3394.repertoireapi.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    @Test
    void should_return_page_of_songs() {
        Pageable pageable = PageRequest.of(0, 5);
        when(songRepository.findAll(any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.getAllSongs(pageable);

        verify(songRepository, times(1)).findAll(any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_saved_song() {
        when(songRepository.save(any(Song.class))).thenReturn(getDoveSono());

        Song song = songService.saveSong(getDoveSono());

        verify(songRepository, times(1)).save(any(Song.class));
        assertNotNull(song);
        assertEquals("Dove sono i bei momenti", song.getTitle());
        assertEquals("Wolfgang Amadeus Mozart", song.getComposer().getName());
    }

    @Test
    void should_return_song_by_id() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(getDoveSono()));

        Song song = songService.findSongById(1L).get();

        verify(songRepository, times(1)).findById(anyLong());
        assertTrue(songService.findSongById(1L).isPresent());
        assertEquals("Dove sono i bei momenti", song.getTitle());
        assertEquals("Wolfgang Amadeus Mozart", song.getComposer().getName());
    }

    @Test
    void verify_delete_method_called() {
        songService.deleteSong(1L);
        verify(songRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void should_return_page_of_songs_in_user_repertoire() {
        Pageable pageable = PageRequest.of(0, 5);
        when(songRepository.findAllSongsInRepertoireByAppUser_Id(anyLong(), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInUserRepertoire(1L, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoireByAppUser_Id(anyLong(), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_by_title() {
        Pageable pageable = PageRequest.of(0, 5);
        when(songRepository.findAllByTitleContainingOrderByTitle(anyString(), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.searchSongsByTitle("dov", pageable);

        verify(songRepository, times(1)).findAllByTitleContainingOrderByTitle(anyString(), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_by_composer() {
        Pageable pageable = PageRequest.of(0, 5);
        when(songRepository.findAllByComposer_NameContains(anyString(), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.searchSongsByComposer("moz", pageable);

        verify(songRepository, times(1)).findAllByComposer_NameContains(anyString(), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_status() {
        Pageable pageable = PageRequest.of(0, 5);
        when(songRepository.findAllSongsInRepertoireByUserWithStatus(anyLong(), any(Status.class), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInUserRepertoireByStatus(1L, Status.PERFORMED, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoireByUserWithStatus(anyLong(), any(Status.class), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_language() {
        Pageable pageable = PageRequest.of(0, 5);
        when(songRepository.findAllSongsInRepertoireByUserByLanguage(anyLong(), any(Language.class), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInUserRepertoireByLanguage(1L, Language.ITALIAN, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoireByUserByLanguage(anyLong(), any(Language.class), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_composer() {
        Pageable pageable = PageRequest.of(0, 5);
        when(songRepository.findAllSongsInRepertoireByUserByComposer(anyLong(), anyLong(), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInUserRepertoireByComposer(1L, 1L, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoireByUserByComposer(anyLong(), anyLong(), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_epoch() {
        Pageable pageable = PageRequest.of(0, 5);
        when(songRepository.findAllSongsInRepertoireByUserByEpoch(anyLong(), any(Epoch.class), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInUserRepertoireByEpoch(1L, Epoch.CLASSICAL, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoireByUserByEpoch(anyLong(), any(Epoch.class), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
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

    private Page<Song> getAllSongs() {
        Song doveSono = getDoveSono();
        Song nessun = getNessunDorma();
        List<Song> songs = new ArrayList<>();
        songs.add(doveSono);
        songs.add(nessun);

        Pageable pageable = PageRequest.of(0, 5);

        return new PageImpl<>(songs, pageable, songs.size());
    }
}

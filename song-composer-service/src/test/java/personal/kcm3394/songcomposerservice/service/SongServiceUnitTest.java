package personal.kcm3394.songcomposerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import personal.kcm3394.songcomposerservice.model.*;
import personal.kcm3394.songcomposerservice.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SongServiceUnitTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongServiceImpl songService;

    private final Pageable pageable = PageRequest.of(0, 5);

    @Test
    void should_return_page_of_songs() {
        when(songRepository.findAll(any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.getAllSongs(pageable);

        verify(songRepository, times(1)).findAll(any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_song_by_id() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(buildDoveSono()));

        Song song = songService.findSongById(1L).get();

        verify(songRepository, times(1)).findById(anyLong());
        assertTrue(songService.findSongById(1L).isPresent());
        assertEquals("Dove sono i bei momenti", song.getTitle());
        assertEquals("Wolfgang Amadeus Mozart", song.getComposer().getName());
    }

    @Test
    void should_return_song_by_title_and_composer() {
        when(songRepository.findByTitleAndComposerId(anyString(), anyLong())).thenReturn(buildDoveSono());

        Song song = songService.findSongByTitleAndComposer("Dove sono i bei momenti", 1L);

        verify(songRepository, times(1)).findByTitleAndComposerId(anyString(), anyLong());
        assertNotNull(song);
        assertEquals("Dove sono i bei momenti", song.getTitle());
        assertEquals("Wolfgang Amadeus Mozart", song.getComposer().getName());
    }

    @Test
    void should_return_saved_song() {
        when(songRepository.save(any(Song.class))).thenReturn(buildDoveSono());

        Song song = songService.saveSong(buildDoveSono());

        verify(songRepository, times(1)).save(any(Song.class));
        assertNotNull(song);
        assertEquals("Dove sono i bei momenti", song.getTitle());
        assertEquals("Wolfgang Amadeus Mozart", song.getComposer().getName());
    }

    @Test
    void verify_delete_method_called() {
        songService.deleteSong(1L);
        verify(songRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void should_return_page_of_songs_by_title() {
        when(songRepository.findAllByTitleContainingOrderByTitle(anyString(), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.searchSongsByTitle("dov", pageable);

        verify(songRepository, times(1)).findAllByTitleContainingOrderByTitle(anyString(), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_by_composer() {
        when(songRepository.findAllByComposer_NameContains(anyString(), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.searchSongsByComposer("moz", pageable);

        verify(songRepository, times(1)).findAllByComposer_NameContains(anyString(), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_in_user_rep() {
        when(songRepository.findAllSongsInRepertoire(anyLong(), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInRepertoire(1L, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoire(anyLong(), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_language() {
        when(songRepository.findAllSongsInRepertoireByLanguage(anyLong(), any(Language.class), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInRepertoireByLanguage(1L, Language.ITALIAN, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoireByLanguage(anyLong(), any(Language.class), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_composer() {
        when(songRepository.findAllSongsInRepertoireByComposer(anyLong(), anyLong(), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInRepertoireByComposer(1L, 1L, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoireByComposer(anyLong(), anyLong(), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_epoch() {
        when(songRepository.findAllSongsInRepertoireByEpoch(anyLong(), any(Epoch.class), any(Pageable.class))).thenReturn(getAllSongs());

        Page<Song> page = songService.findAllSongsInRepertoireByEpoch(1L, Epoch.CLASSICAL, pageable);

        verify(songRepository, times(1)).findAllSongsInRepertoireByEpoch(anyLong(), any(Epoch.class), any(Pageable.class));
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    private Composer buildMozart() {
        return Composer.builder()
                .id(1L)
                .name("Wolfgang Amadeus Mozart")
                .epoch(Epoch.CLASSICAL)
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
                .epoch(Epoch.LATE_ROMANTIC)
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

    private Page<Song> getAllSongs() {
        Song doveSono = buildDoveSono();
        Song nessun = buildNessunDorma();
        List<Song> songs = new ArrayList<>();
        songs.add(doveSono);
        songs.add(nessun);

        Pageable pageable = PageRequest.of(0, 5);

        return new PageImpl<>(songs, pageable, songs.size());
    }
}

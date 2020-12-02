package personal.kcm3394.songcomposerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import personal.kcm3394.songcomposerservice.model.Repertoire;
import personal.kcm3394.songcomposerservice.model.Song;
import personal.kcm3394.songcomposerservice.repository.RepertoireRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RepertoireServiceUnitTest {

    @Mock
    private RepertoireRepository repertoireRepository;

    @InjectMocks
    private RepertoireServiceImpl repertoireService;

    @Test
    void should_return_repertoire_by_id() {
        when(repertoireRepository.findById(anyLong())).thenReturn(Optional.of(buildRepertoire()));

        Repertoire repertoire = repertoireService.findRepertoireById(1L).get();

        verify(repertoireRepository, times(1)).findById(anyLong());
        assertTrue(repertoireService.findRepertoireById(1L).isPresent());
        assertEquals(1L, repertoire.getUserId());
        assertEquals(2, repertoire.getRepertoire().size());
    }

    @Test
    void should_return_repertoire_by_user_id() {
        when(repertoireRepository.findByUserId(anyLong())).thenReturn(buildRepertoire());

        Repertoire repertoire = repertoireService.findRepertoireByUserId(1L);

        verify(repertoireRepository, times(1)).findByUserId(anyLong());
        assertNotNull(repertoire);
        assertEquals(1L, repertoire.getUserId());
        assertEquals(2, repertoire.getRepertoire().size());
    }

    @Test
    void should_return_saved_repertoire() {
        when(repertoireRepository.save(any(Repertoire.class))).thenReturn(buildRepertoire());

        Repertoire repertoire = repertoireService.saveRepertoire(buildRepertoire());

        verify(repertoireRepository, times(1)).save(any(Repertoire.class));
        assertNotNull(repertoire);
        assertEquals(1L, repertoire.getUserId());
        assertEquals(2, repertoire.getRepertoire().size());
    }

    @Test
    void verify_delete_method_called() {
        repertoireService.deleteRepertoire(1L);
        verify(repertoireRepository, times(1)).deleteById(anyLong());
    }

    private Repertoire buildRepertoire() {
        Song s1 = Song.builder()
                .id(1L)
                .title("First")
                .build();

        Song s2 = Song.builder()
                .id(2L)
                .title("Second")
                .build();

        Set<Song> songs = new HashSet<>();
        songs.add(s1);
        songs.add(s2);

        return Repertoire.builder()
                .id(1L)
                .userId(1L)
                .repertoire(songs)
                .build();
    }
}

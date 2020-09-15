package personal.kcm3394.repertoireapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Notes;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Status;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NotesRepositoryTest {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Notes note;
    private Song song;
    private Composer comp;
    private AppUser user;

    @BeforeEach
    void setUp() {
        note = new Notes();
        note.setStatus(Status.PERFORMED);

        song = new Song();
        song.setTitle("Ave Maria");
        comp = new Composer();
        comp.setName("Franz Schubert");
        song.setComposer(comp);

        note.setSong(song);

        user = new AppUser();
        user.setUsername("test");
        user.setPassword("testPassword");

        note.setUser(user);
    }

    @Test
    void should_save_note_and_give_id() {
        Notes saved = notesRepository.save(note);

        assertTrue(notesRepository.findById(saved.getId()).isPresent());
        assertThat(notesRepository.findById(saved.getId()).get(), equalTo(saved));
        assertThat(notesRepository.findById(saved.getId()).get().getId(), isA(Long.class));
    }

    @Test
    void should_update_note() {
        Notes saved = notesRepository.save(note);

        note.setStatus(Status.LEARNING);
        Notes changed = notesRepository.save(note);

        assertThat(changed.getId(), is(note.getId()));
        assertTrue(notesRepository.findById(note.getId()).isPresent());
        assertThat(notesRepository.findById(note.getId()).get().getStatus(), is(Status.LEARNING));
    }

    @Test
    void should_delete_note() {
        testEntityManager.persist(comp);
        testEntityManager.persist(song);
        testEntityManager.persist(user);
        testEntityManager.persistAndFlush(note);
        notesRepository.deleteById(note.getId());

        assertFalse(notesRepository.findById(note.getId()).isPresent());
    }

    @Test
    void should_find_note_by_user_id_song_id() {
        testEntityManager.persist(comp);
        testEntityManager.persist(song);
        testEntityManager.persist(user);
        testEntityManager.persistAndFlush(note);
        Notes found = notesRepository.findByUser_IdAndSong_Id(user.getId(), song.getId());

        assertNotNull(found);
        assertThat(found.getId(), equalTo(note.getId()));
    }
}

package personal.kcm3394.songcomposerservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import personal.kcm3394.songcomposerservice.model.Repertoire;
import personal.kcm3394.songcomposerservice.model.Song;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RepertoireRepositoryUnitTest {

    @Autowired
    private RepertoireRepository repertoireRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Repertoire repertoire;
    private Set<Song> sampleRep;

    @BeforeEach
    void setUp() {
        sampleRep = new HashSet<>();
        Song s1 = Song.builder()
                .title("First")
                .build();
        testEntityManager.persist(s1);
        sampleRep.add(s1);

        Song s2 = Song.builder()
                .title("Second")
                .build();
        testEntityManager.persist(s2);
        sampleRep.add(s2);

        testEntityManager.flush();

        repertoire = Repertoire.builder()
                .userId(1L)
                .repertoire(sampleRep)
                .build();
        testEntityManager.persistAndFlush(repertoire);
    }

    @Test
    void should_save_repertoire_and_give_id() {
        Repertoire newRep = Repertoire.builder()
                .userId(2L)
                .repertoire(sampleRep)
                .build();
        Repertoire saved = repertoireRepository.save(newRep);

        assertTrue(repertoireRepository.findById(saved.getId()).isPresent());
        assertThat(repertoireRepository.findById(saved.getId()).get(), equalTo(saved));
        assertThat(repertoireRepository.findById(saved.getId()).get().getId(), isA(Long.class));
        assertEquals(repertoireRepository.findById(saved.getId()).get().getRepertoire().size(), 2);
    }

    @Test
    void should_update_repertoire() {
        Song s3 = Song.builder()
                .title("Third")
                .build();
        testEntityManager.persistAndFlush(s3);
        repertoire.getRepertoire().add(s3);
        Repertoire updated = repertoireRepository.save(repertoire);

        assertTrue(repertoireRepository.findById(updated.getId()).isPresent());
        assertEquals(repertoireRepository.findById(updated.getId()).get().getRepertoire().size(), 3);
    }

    @Test
    void should_return_repertoire_by_id() {
        Optional<Repertoire> foundRepertoire = repertoireRepository.findById(repertoire.getId());

        assertTrue(foundRepertoire.isPresent());
        assertThat(foundRepertoire.get(), equalTo(repertoire));
    }

    @Test
    void should_delete_repertoire() {
        repertoireRepository.deleteById(repertoire.getId());

        assertFalse(repertoireRepository.findById(repertoire.getId()).isPresent());
    }

    @Test
    void should_return_repertoire_by_user_id() {
        Repertoire found = repertoireRepository.findByUserId(1L);

        assertNotNull(found);
        assertThat(repertoireRepository.findById(found.getId()).get(), equalTo(found));
    }
}

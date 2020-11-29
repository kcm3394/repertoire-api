package personal.kcm3394.songcomposerservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import personal.kcm3394.songcomposerservice.model.Composer;
import personal.kcm3394.songcomposerservice.model.Epoch;
import personal.kcm3394.songcomposerservice.model.Language;
import personal.kcm3394.songcomposerservice.model.Song;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class SongRepositoryUnitTest {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Song s1;
    private Song s2;
    private Composer c1;
    private final Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    void setUp() {
        s1 = Song.builder()
                .title("song1")
                .language(Language.FRENCH)
                .build();

        c1 = Composer.builder()
                .name("comp1")
                .epoch(Epoch.CLASSICAL)
                .build();

        s1.setComposer(c1);
        testEntityManager.persist(c1);
        testEntityManager.persist(s1);

        s2 = Song.builder()
                .title("song2")
                .language(Language.ITALIAN)
                .build();

        Composer c2 = Composer.builder()
                .name("comp2")
                .epoch(Epoch.ROMANTIC)
                .build();

        s2.setComposer(c2);
        testEntityManager.persist(c2);
        testEntityManager.persist(s2);

        Song s3 = Song.builder()
                .title("different3")
                .language(Language.ITALIAN)
                .build();

        s3.setComposer(c2);
        testEntityManager.persist(s3);
    }

    @Test
    void should_return_page_of_all_songs() {
        Page<Song> page = songRepository.findAll(pageable);

        assertThat(page.getNumberOfElements(), is(3));
        assertThat(page.getContent().get(0).getTitle(), is("song1"));
    }

    @Test
    void should_return_saved_song() {
        Song toSave = new Song();
        toSave.setTitle("saved");
        toSave.setComposer(c1);

        Song saved = songRepository.save(toSave);

        assertTrue(songRepository.findById(saved.getId()).isPresent());
        assertThat(songRepository.findById(saved.getId()).get(), equalTo(saved));
        assertThat(songRepository.findById(saved.getId()).get().getId(), isA(Long.class));
    }

    @Test
    void should_update_song() {
        s2.setTitle("changed");
        s2.setComposer(c1);

        Song changed = songRepository.save(s2);

        assertThat(changed.getId(), is(s2.getId()));
        assertTrue(songRepository.findById(s2.getId()).isPresent());
        assertThat(songRepository.findById(s2.getId()).get().getTitle(), is("changed"));
        assertThat(songRepository.findById(s2.getId()).get().getComposer(), is(c1));
    }

    @Test
    void should_return_song_by_id() {
        Optional<Song> foundSong = songRepository.findById(s1.getId());

        assertTrue(foundSong.isPresent());
        assertThat(foundSong.get(), equalTo(s1));
    }

    @Test
    void should_delete_song() {
        songRepository.deleteById(s1.getId());

        assertFalse(songRepository.findById(s1.getId()).isPresent());
    }

    @Test
    void should_return_page_of_songs_by_title_fragment() {
        Page<Song> page = songRepository.findAllByTitleContainingOrderByTitle("son", pageable);

        assertThat(page.getNumberOfElements(), is(2));
        assertThat(page.getContent().get(0).getTitle(), is("song1"));
    }

    @Test
    void should_return_page_of_songs_by_composer_name_fragment_in_alphabetical_order() {
        Page<Song> page = songRepository.findAllByComposer_NameContains("%comp%", pageable);

        assertThat(page.getNumberOfElements(), is(3));
        assertThat(page.getContent().get(0).getTitle(), is("different3"));
    }
}

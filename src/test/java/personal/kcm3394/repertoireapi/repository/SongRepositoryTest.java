package personal.kcm3394.repertoireapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Notes;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SongRepositoryTest {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Song s1;
    private Song s2;
    private Composer c1;
    private AppUser user;
    private final Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    void setUp() {
        s1 = new Song();
        s1.setTitle("song1");
        s1.setLanguage(Language.FRENCH);

        c1 = new Composer();
        c1.setName("comp1");
        c1.setEpoch(Epoch.CLASSICAL);
        s1.setComposer(c1);
        testEntityManager.persist(c1);
        testEntityManager.persist(s1);

        s2 = new Song();
        s2.setTitle("song2");
        s2.setLanguage(Language.ITALIAN);

        Composer c2 = new Composer();
        c2.setName("comp2");
        c2.setEpoch(Epoch.ROMANTIC);
        s2.setComposer(c2);
        testEntityManager.persist(c2);
        testEntityManager.persist(s2);

        Song s3 = new Song();
        s3.setTitle("different3");
        s3.setLanguage(Language.ITALIAN);
        s3.setComposer(c2);
        testEntityManager.persist(s3);

        user = new AppUser();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setRepertoire(new HashSet<>(Arrays.asList(s1, s2)));
        testEntityManager.persist(user);

        Notes n1 = new Notes();
        n1.setUser(user);
        n1.setSong(s1);
        n1.setStatus(Status.PERFORMED);
        testEntityManager.persist(n1);

        Notes n2 = new Notes();
        n2.setUser(user);
        n2.setSong(s2);
        n2.setStatus(Status.PERFORMED);
        testEntityManager.persist(n2);

        Notes n3 = new Notes();
        n3.setUser(user);
        n3.setSong(s3);
        n3.setStatus(Status.LEARNING);
        testEntityManager.persistAndFlush(n3);
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
    void should_return_page_of_songs_in_user_rep() {
        Page<Song> page = songRepository.findAllSongsInRepertoireByAppUser_Id(user.getId(), pageable);

        assertThat(page.getNumberOfElements(), is(2));
        assertThat(page.getContent().get(0).getTitle(), is("song1"));
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

    @Test
    void should_return_page_of_songs_in_user_rep_by_status() {
        Page<Song> page = songRepository.findAllSongsInRepertoireByUserWithStatus(user.getId(), Status.PERFORMED, pageable);

        assertThat(page.getNumberOfElements(), is(2));
        assertThat(page.getContent().get(0).getTitle(), is("song1"));
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_language() {
        Page<Song> page = songRepository.findAllSongsInRepertoireByUserByLanguage(user.getId(), Language.ITALIAN, pageable);

        assertThat(page.getNumberOfElements(), is(1));
        assertThat(page.getContent().get(0).getTitle(), is("song2"));
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_composer() {
        Page<Song> page = songRepository.findAllSongsInRepertoireByUserByComposer(user.getId(), c1.getId(), pageable);

        assertThat(page.getNumberOfElements(), is(1));
        assertThat(page.getContent().get(0).getTitle(), is("song1"));
    }

    @Test
    void should_return_page_of_songs_in_user_rep_by_epoch() {
        Page<Song> page = songRepository.findAllSongsInRepertoireByUserByEpoch(user.getId(), Epoch.CLASSICAL, pageable);

        assertThat(page.getNumberOfElements(), is(1));
        assertThat(page.getContent().get(0).getTitle(), is("song1"));
    }
}

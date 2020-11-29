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
import personal.kcm3394.songcomposerservice.model.Song;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ComposerRepositoryUnitTest {

    @Autowired
    private ComposerRepository composerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Composer c1;
    private Composer c2;
    private Composer c3;
    private final Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    void setUp() {
        c1 = Composer.builder()
                .name("Robert Schumann")
                .epoch(Epoch.ROMANTIC)
                .build();
        testEntityManager.persist(c1);

        c2 = Composer.builder()
                .name("Franz Schubert")
                .epoch(Epoch.CLASSICAL)
                .build();
        testEntityManager.persist(c2);

        c3 = Composer.builder()
                .name("Wolfgang Amadeus Mozart")
                .epoch(Epoch.CLASSICAL)
                .build();
        testEntityManager.persist(c3);
        testEntityManager.flush();
    }

    @Test
    void should_find_all_composers() {
        Page<Composer> page = composerRepository.findAll(pageable);

        assertThat(page.getNumberOfElements(), is(3));
        assertThat(page.getContent().get(0).getName(), is("Robert Schumann"));
    }

    @Test
    void should_save_composer_and_give_id() {
        Composer c4 = new Composer();
        c4.setName("comp4");
        Composer saved = composerRepository.save(c4);

        assertTrue(composerRepository.findById(saved.getId()).isPresent());
        assertThat(composerRepository.findById(saved.getId()).get(), equalTo(saved));
        assertThat(composerRepository.findById(saved.getId()).get().getId(), isA(Long.class));
    }

    @Test
    void should_update_composer() {
        c3.setName("Mozart");
        Composer saved = composerRepository.save(c3);
        assertTrue(composerRepository.findById(saved.getId()).isPresent());
        String savedName = composerRepository.findById(saved.getId()).get().getName();
        Long savedId = composerRepository.findById(saved.getId()).get().getId();

        assertThat(savedName, is("Mozart"));
        assertThat(savedId, is(c3.getId()));
    }

    @Test
    void should_return_composer_by_id() {
        Optional<Composer> foundComposer = composerRepository.findById(c1.getId());

        assertTrue(foundComposer.isPresent());
        assertThat(foundComposer.get(), equalTo(c1));
    }

    @Test
    void should_delete_composer_by_id() {
        composerRepository.deleteById(c1.getId());

        assertFalse(composerRepository.findById(c1.getId()).isPresent());
    }

    @Test
    void should_return_composers_by_name_fragment_in_alphabetical_order() {
        Page<Composer> page = composerRepository.findAllByNameContainingOrderByName("Schu", pageable);

        assertNotNull(page);
        assertThat(page.getNumberOfElements(), is(2));
        assertThat(page.getContent().get(0).getName(), is("Franz Schubert"));
    }

    @Test
    void should_return_composers_by_epoch_in_alphabetical_order() {
        Page<Composer> page = composerRepository.findAllByEpochOrderByName(Epoch.CLASSICAL, pageable);

        assertNotNull(page);
        assertThat(page.getNumberOfElements(), is(2));
        assertThat(page.getContent().get(0).getName(), is("Franz Schubert"));
    }

    @Test
    void should_return_composers_by_composition_title_containing_fragment_in_composer_alphabetical_order() {
        Song s1 = Song.builder()
                .title("Ave Maria")
                .composer(c1)
                .build();
        testEntityManager.persist(s1);

        Song s2 = Song.builder()
                .title("Ave Verum")
                .composer(c2)
                .build();
        testEntityManager.persist(s2);

        Song s3 = Song.builder()
                .title("Different")
                .composer(c3)
                .build();
        testEntityManager.persist(s3);
        testEntityManager.flush();

        Page<Composer> page = composerRepository.findAllByCompositions_TitleContains("%Ave%", pageable);

        assertNotNull(page);
        assertThat(page.getNumberOfElements(), is(2));
        assertThat(page.getContent().get(0).getName(), is("Franz Schubert"));
    }
}

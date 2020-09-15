package personal.kcm3394.repertoireapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ComposerRepositoryTest {

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
        c1 = new Composer();
        c1.setName("Robert Schumann");
        c1.setEpoch(Epoch.ROMANTIC);
        testEntityManager.persist(c1);

        c2 = new Composer();
        c2.setName("Franz Schubert");
        c2.setEpoch(Epoch.CLASSICAL);
        testEntityManager.persist(c2);

        c3 = new Composer();
        c3.setName("Wolfgang Amadeus Mozart");
        c3.setEpoch(Epoch.CLASSICAL);
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
        Song s1 = new Song();
        s1.setTitle("Ave Maria");
        s1.setComposer(c1);
        testEntityManager.persist(s1);

        Song s2 = new Song();
        s2.setTitle("Ave Verum");
        s2.setComposer(c2);
        testEntityManager.persist(s2);

        Song s3 = new Song();
        s3.setTitle("Different");
        s3.setComposer(c3);
        testEntityManager.persist(s3);
        testEntityManager.flush();

        Page<Composer> page = composerRepository.findAllByCompositions_TitleContains("%Ave%", pageable);

        assertNotNull(page);
        assertThat(page.getNumberOfElements(), is(2));
        assertThat(page.getContent().get(0).getName(), is("Franz Schubert"));
    }
}

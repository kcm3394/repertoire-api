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
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.repository.ComposerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ComposerServiceTest {
    
    @Mock
    private ComposerRepository composerRepository;
    
    @InjectMocks
    private ComposerService composerService;

    @Test
    void should_return_page_of_composers() {
        Pageable pageable = PageRequest.of(0, 5);
        when(composerRepository.findAll(any(Pageable.class))).thenReturn(getAllComposers());

        Page<Composer> page = composerService.getAllComposers(pageable);

        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_saved_composer() {
        when(composerRepository.save(any(Composer.class))).thenReturn(getComposerMozart());

        Composer saved = composerService.saveComposer(getComposerMozart());

        assertEquals("Wolfgang Amadeus Mozart", saved.getName());
        assertEquals(Epoch.CLASSICAL, saved.getEpoch());
        assertEquals(1L, saved.getId());
    }

    @Test
    void should_return_composer_by_id() {
        when(composerService.findComposerById(anyLong())).thenReturn(Optional.of(getComposerMozart()));

        assertTrue(composerService.findComposerById(1L).isPresent());
        Composer found = composerService.findComposerById(1L).get();

        assertEquals(1L, found.getId());
        assertEquals("Wolfgang Amadeus Mozart", found.getName());
        assertEquals(Epoch.CLASSICAL, found.getEpoch());
    }

    @Test
    void verify_delete_method_called() {
        composerService.deleteComposer(1L);
        verify(composerRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void should_return_page_of_composers_by_name() {
        Pageable pageable = PageRequest.of(0, 5);
        when(composerRepository.findAllByNameContainingOrderByName(anyString(), any(Pageable.class))).thenReturn(getAllComposers());

        Page<Composer> page = composerService.searchComposersByName("moz", pageable);

        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_composers_by_epoch() {
        Pageable pageable = PageRequest.of(0, 5);
        when(composerRepository.findAllByEpochOrderByName(any(Epoch.class), any(Pageable.class))).thenReturn(getAllComposers());

        Page<Composer> page = composerService.searchComposersByEpoch("classical", pageable);

        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    @Test
    void should_return_page_of_composers_by_composition() {
        Pageable pageable = PageRequest.of(0, 5);
        when(composerRepository.findAllByCompositions_TitleContains(anyString(), any(Pageable.class))).thenReturn(getAllComposers());

        Page<Composer> page = composerService.searchComposersByComposition("dove", pageable);

        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1L, page.getContent().get(0).getId());
    }

    private Composer getComposerMozart() {
        Composer mozart = new Composer();
        mozart.setId(1L);
        mozart.setName("Wolfgang Amadeus Mozart");
        mozart.setBirthDate(LocalDate.of(1756, 1, 1));
        mozart.setDeathDate(LocalDate.of(1791, 1, 1));
        mozart.setEpoch(Epoch.CLASSICAL);

        return mozart;
    }

    private Composer getComposerPuccini() {
        Composer puccini = new Composer();
        puccini.setId(2L);
        puccini.setName("Giacomo Puccini");
        puccini.setBirthDate(LocalDate.of(1858, 12, 22));
        puccini.setDeathDate(LocalDate.of(1924, 11, 29));
        puccini.setEpoch(Epoch.LATE_ROMANTIC);

        return puccini;
    }

    private Page<Composer> getAllComposers() {
        Composer mozart = getComposerMozart();
        Composer puccini = getComposerPuccini();
        List<Composer> composers = new ArrayList<>();
        composers.add(mozart);
        composers.add(puccini);

        Pageable pageable = PageRequest.of(0, 5);

        return new PageImpl<>(composers, pageable, composers.size());
    }
}

//https://medium.com/backend-habit/integrate-junit-and-mockito-unit-testing-for-service-layer-a0a5a811c58a

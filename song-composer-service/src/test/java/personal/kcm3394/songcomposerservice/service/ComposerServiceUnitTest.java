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
import personal.kcm3394.songcomposerservice.model.Composer;
import personal.kcm3394.songcomposerservice.model.Epoch;
import personal.kcm3394.songcomposerservice.repository.ComposerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ComposerServiceUnitTest {

    @Mock
    private ComposerRepository composerRepository;

    @InjectMocks
    private ComposerServiceImpl composerService;

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
        when(composerRepository.save(any(Composer.class))).thenReturn(buildMozart());

        Composer saved = composerService.saveComposer(buildMozart());

        assertEquals("Wolfgang Amadeus Mozart", saved.getName());
        assertEquals(Epoch.CLASSICAL, saved.getEpoch());
        assertEquals(1L, saved.getId());
    }

    @Test
    void should_return_composer_by_id() {
        when(composerService.findComposerById(anyLong())).thenReturn(Optional.of(buildMozart()));

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

    private Composer buildMozart() {
        return Composer.builder()
                .id(1L)
                .name("Wolfgang Amadeus Mozart")
                .birthDate(LocalDate.of(1756, 1, 1))
                .deathDate(LocalDate.of(1791, 1, 1))
                .epoch(Epoch.CLASSICAL)
                .build();
    }

    private Composer buildPuccini() {
        return Composer.builder()
                .id(2L)
                .name("Giacomo Puccini")
                .birthDate(LocalDate.of(1858, 12, 22))
                .deathDate(LocalDate.of(1924, 11, 29))
                .epoch(Epoch.LATE_ROMANTIC)
                .build();
    }

    private Page<Composer> getAllComposers() {
        Composer mozart = buildMozart();
        Composer puccini = buildPuccini();
        List<Composer> composers = new ArrayList<>();
        composers.add(mozart);
        composers.add(puccini);

        Pageable pageable = PageRequest.of(0, 5);

        return new PageImpl<>(composers, pageable, composers.size());
    }
}

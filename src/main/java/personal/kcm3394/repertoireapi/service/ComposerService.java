package personal.kcm3394.repertoireapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.repository.ComposerRepository;

import java.util.List;

/**
 * Makes calls to the database layer related to CRUD operations for Composers
 */
@Service
@Transactional
public class ComposerService {

    private final ComposerRepository composerRepository;

    public ComposerService(ComposerRepository composerRepository) {
        this.composerRepository = composerRepository;
    }

    public Page<Composer> getAllComposers(Pageable pageable) {
        return composerRepository.findAll(pageable);
    }

    public Composer saveComposer(Composer composer) {
        return composerRepository.save(composer);
    }

    public Composer findComposerById(Long composerId) {
        return composerRepository.findById(composerId).orElse(null);
    }

    public void deleteComposer(Long composerId) {
        composerRepository.deleteById(composerId);
    }

    public Page<Composer> searchComposersByName(String nameFragment, Pageable pageable) {
        return composerRepository.findAllByNameContainingOrderByName(nameFragment, pageable);
    }

    public Page<Composer> searchComposersByEpoch(String epoch, Pageable pageable) {
        epoch = epoch.toUpperCase();
        return composerRepository.findAllByEpochOrderByName(Epoch.valueOf(epoch), pageable);
    }

    public Page<Composer> searchComposersByComposition(String titleFragment, Pageable pageable) {
        return composerRepository.findAllByCompositions_TitleContains("%" + titleFragment + "%", pageable);
    }


}

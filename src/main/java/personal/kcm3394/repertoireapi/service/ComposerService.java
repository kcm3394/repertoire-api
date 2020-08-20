package personal.kcm3394.repertoireapi.service;

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

    public List<Composer> getAllComposers() {
        return composerRepository.findAll();
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

    public List<Composer> searchComposersByName(String nameFragment) {
        return composerRepository.findAllByNameContainingOrderByName(nameFragment);
    }

    public List<Composer> searchComposersByEpoch(String epoch) {
        epoch = epoch.toUpperCase();
        return composerRepository.findAllByEpochOrderByName(Epoch.valueOf(epoch));
    }

    public List<Composer> searchComposersByComposition(String titleFragment) {
        return composerRepository.findAllByCompositions_TitleContains("%" + titleFragment + "%");
    }


}

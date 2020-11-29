package personal.kcm3394.songcomposerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.songcomposerservice.model.Composer;
import personal.kcm3394.songcomposerservice.model.Epoch;
import personal.kcm3394.songcomposerservice.repository.ComposerRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ComposerServiceImpl implements ComposerService {

    private final ComposerRepository composerRepository;

    @Override
    public Page<Composer> getAllComposers(Pageable pageable) {
        return composerRepository.findAll(pageable);
    }

    @Override
    public Optional<Composer> findComposerById(Long composerId) {
        return composerRepository.findById(composerId);
    }

    @Override
    public Composer saveComposer(Composer composer) {
        return composerRepository.save(composer);
    }

    @Override
    public void deleteComposer(Long composerId) {
        composerRepository.deleteById(composerId);
    }

    @Override
    public Page<Composer> searchComposersByName(String nameFragment, Pageable pageable) {
        return composerRepository.findAllByNameContainingOrderByName(nameFragment, pageable);
    }

    @Override
    public Page<Composer> searchComposersByEpoch(String epoch, Pageable pageable) {
        return composerRepository.findAllByEpochOrderByName(Epoch.valueOf(epoch.toUpperCase()), pageable);
    }

    @Override
    public Page<Composer> searchComposersByComposition(String titleFragment, Pageable pageable) {
        return composerRepository.findAllByCompositions_TitleContains("%" + titleFragment + "%", pageable);
    }
}

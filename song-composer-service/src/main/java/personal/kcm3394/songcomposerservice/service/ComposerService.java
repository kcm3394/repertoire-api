package personal.kcm3394.songcomposerservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.kcm3394.songcomposerservice.model.Composer;

import java.util.Optional;

public interface ComposerService {

    Page<Composer> getAllComposers(Pageable pageable);

    Optional<Composer> findComposerById(Long composerId);

    Composer saveComposer(Composer composer);

    void deleteComposer(Long composerId);

    Page<Composer> searchComposersByName(String nameFragment, Pageable pageable);

    Page<Composer> searchComposersByEpoch(String epoch, Pageable pageable);

    Page<Composer> searchComposersByComposition(String titleFragment, Pageable pageable);
}

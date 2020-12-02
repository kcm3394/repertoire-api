package personal.kcm3394.songcomposerservice.service;

import personal.kcm3394.songcomposerservice.model.Repertoire;

import java.util.Optional;

public interface RepertoireService {

    Optional<Repertoire> findRepertoireById(Long repId);

    Repertoire findRepertoireByUserId(Long userId);

    Repertoire saveRepertoire(Repertoire repertoire);

    void deleteRepertoire(Long repId);
}

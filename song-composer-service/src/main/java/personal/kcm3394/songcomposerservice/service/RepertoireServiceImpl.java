package personal.kcm3394.songcomposerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.songcomposerservice.model.Repertoire;
import personal.kcm3394.songcomposerservice.repository.RepertoireRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RepertoireServiceImpl implements RepertoireService {

    private final RepertoireRepository repertoireRepository;

    @Override
    public Optional<Repertoire> findRepertoireById(Long repId) {
        return repertoireRepository.findById(repId);
        //TODO add an order to the songs it returns
    }

    @Override
    public Repertoire findRepertoireByUserId(Long userId) {
        return repertoireRepository.findByUserId(userId);
    }

    @Override
    public Repertoire saveRepertoire(Repertoire repertoire) {
        return repertoireRepository.save(repertoire);
    }

    //todo if user is deleted, delete repertoire record
    @Override
    public void deleteRepertoire(Long repId) {
        repertoireRepository.deleteById(repId);
    }
}

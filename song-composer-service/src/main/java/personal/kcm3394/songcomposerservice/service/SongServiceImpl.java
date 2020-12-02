package personal.kcm3394.songcomposerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.songcomposerservice.model.Epoch;
import personal.kcm3394.songcomposerservice.model.Language;
import personal.kcm3394.songcomposerservice.model.Song;
import personal.kcm3394.songcomposerservice.repository.SongRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Override
    public Page<Song> getAllSongs(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

    @Override
    public Optional<Song> findSongById(Long songId) {
        return songRepository.findById(songId);
    }

    @Override
    public Song findSongByTitleAndComposer(String title, Long composerId) {
        return songRepository.findByTitleAndComposerId(title, composerId);
    }

    @Override
    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    @Override
    public void deleteSong(Long songId) {
        songRepository.deleteById(songId);
    }

    @Override
    public Page<Song> searchSongsByTitle(String titleFragment, Pageable pageable) {
        return songRepository.findAllByTitleContainingOrderByTitle(titleFragment, pageable);
    }

    @Override
    public Page<Song> searchSongsByComposer(String composerName, Pageable pageable) {
        return songRepository.findAllByComposer_NameContains("%" + composerName + "%", pageable);
    }

    @Override
    public Page<Song> findAllSongsInRepertoire(Long repId, Pageable pageable) {
        return songRepository.findAllSongsInRepertoire(repId, pageable);
    }

    @Override
    public Page<Song> findAllSongsInRepertoireByLanguage(Long repId, Language language, Pageable pageable) {
        return songRepository.findAllSongsInRepertoireByLanguage(repId, language, pageable);
    }

    @Override
    public Page<Song> findAllSongsInRepertoireByComposer(Long repId, Long composerId, Pageable pageable) {
        return songRepository.findAllSongsInRepertoireByComposer(repId, composerId, pageable);
    }

    @Override
    public Page<Song> findAllSongsInRepertoireByEpoch(Long repId, Epoch epoch, Pageable pageable) {
        return songRepository.findAllSongsInRepertoireByEpoch(repId, epoch, pageable);
    }
}

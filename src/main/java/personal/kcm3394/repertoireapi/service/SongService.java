package personal.kcm3394.repertoireapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Status;
import personal.kcm3394.repertoireapi.repository.SongRepository;

/**
 * Makes calls to the database layer related to CRUD operations for Songs
 */
@Service
@Transactional
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Page<Song> getAllSongs(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    public Song findSongById(Long songId) {
        return songRepository.findById(songId).orElse(null);
    }

    public void deleteSong(Long songId) {
        songRepository.deleteById(songId);
    }

    public Page<Song> findAllSongsInUserRepertoire(Long userId, Pageable pageable) {
        return songRepository.findAllSongsInRepertoireByAppUser_Id(userId, pageable);
    }

    public Page<Song> searchSongsByTitle(String titleFragment, Pageable pageable) {
        return songRepository.findAllByTitleContainingOrderByTitle(titleFragment, pageable);
    }

    public Page<Song> searchSongsByComposer(String composerName, Pageable pageable) {
        return songRepository.findAllByComposer_NameContains("%" + composerName + "%", pageable);
    }

    public Page<Song> findAllSongsInUserRepertoireByStatus(Long userId, Status status, Pageable pageable) {
        return songRepository.findAllSongsInRepertoireByUserWithStatus(userId, status, pageable);
    }

    public Page<Song> findAllSongsInUserRepertoireByLanguage(Long userId, Language language, Pageable pageable) {
        return songRepository.findAllSongsInRepertoireByUserByLanguage(userId, language, pageable);
    }

    public Page<Song> findAllSongsInUserRepertoireByComposer(Long userId, Long composerId, Pageable pageable) {
        return songRepository.findAllSongsInRepertoireByUserByComposer(userId, composerId, pageable);
    }

    public Page<Song> findAllSongsInUserRepertoireByEpoch(Long userId, Epoch epoch, Pageable pageable) {
        return songRepository.findAllSongsInRepertoireByUserByEpoch(userId, epoch, pageable);
    }
}

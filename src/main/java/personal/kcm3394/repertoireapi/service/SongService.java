package personal.kcm3394.repertoireapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Status;
import personal.kcm3394.repertoireapi.repository.SongRepository;

import java.util.List;

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

    public List<Song> getAllSongs() {
        return songRepository.findAll();
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

    public List<Song> findAllSongsInUserRepertoire(Long userId) {
        return songRepository.findAllSongsInRepertoireByAppUser_Id(userId);
    }

    public List<Song> searchSongsByTitle(String titleFragment) {
        return songRepository.findAllByTitleContainingOrderByTitle(titleFragment);
    }

    public List<Song> searchSongsByComposer(String composerName) {
        return songRepository.findAllByComposer_NameContains("%" + composerName + "%");
    }

    public List<Song> findAllSongsInUserRepertoireByStatus(Long userId, Status status) {
        return songRepository.findAllSongsInRepertoireByUserWithStatus(userId, status);
    }

    public List<Song> findAllSongsInUserRepertoireByLanguage(Long userId, Language language) {
        return songRepository.findAllSongsInRepertoireByUserByLanguage(userId, language);
    }

    public List<Song> findAllSongsInUserRepertoireByComposer(Long userId, Long composerId) {
        return songRepository.findAllSongsInRepertoireByUserByComposer(userId, composerId);
    }

    public List<Song> findAllSongsInUserRepertoireByEpoch(Long userId, Epoch epoch) {
        return songRepository.findAllSongsInRepertoireByUserByEpoch(userId, epoch);
    }
}

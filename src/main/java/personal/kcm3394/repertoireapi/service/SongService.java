package personal.kcm3394.repertoireapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.repertoireapi.domain.Song;
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
}

package personal.kcm3394.songcomposerservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.kcm3394.songcomposerservice.model.Song;

import java.util.Optional;

public interface SongService {

    Page<Song> getAllSongs(Pageable pageable);

    Optional<Song> findSongById(Long songId);

    Song saveSong(Song song);

    void deleteSong(Long songId);

    Page<Song> searchSongsByTitle(String titleFragment, Pageable pageable);

    Page<Song> searchSongsByComposer(String composerName, Pageable pageable);

}

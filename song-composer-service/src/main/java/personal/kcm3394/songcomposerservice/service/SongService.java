package personal.kcm3394.songcomposerservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.kcm3394.songcomposerservice.model.Epoch;
import personal.kcm3394.songcomposerservice.model.Language;
import personal.kcm3394.songcomposerservice.model.Song;

import java.util.Optional;

public interface SongService {

    Page<Song> getAllSongs(Pageable pageable);

    Optional<Song> findSongById(Long songId);

    Song saveSong(Song song);

    void deleteSong(Long songId);

    Page<Song> searchSongsByTitle(String titleFragment, Pageable pageable);

    Page<Song> searchSongsByComposer(String composerName, Pageable pageable);

    Page<Song> findAllSongsInRepertoire(Long repId, Pageable pageable);

    Page<Song> findAllSongsInRepertoireByLanguage(Long repId, Language language, Pageable pageable);

    Page<Song> findAllSongsInRepertoireByComposer(Long repId, Long composerId, Pageable pageable);

    Page<Song> findAllSongsInRepertoireByEpoch(Long repId, Epoch epoch, Pageable pageable);
}

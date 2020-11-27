package personal.kcm3394.songcomposerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.songcomposerservice.domain.Song;
import personal.kcm3394.songcomposerservice.repository.SongRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
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
}

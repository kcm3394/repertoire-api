package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.dtos.ComposerDTO;
import personal.kcm3394.repertoireapi.domain.dtos.SongDTO;
import personal.kcm3394.repertoireapi.exceptions.NoEntityFoundException;
import personal.kcm3394.repertoireapi.service.ComposerService;
import personal.kcm3394.repertoireapi.service.SongService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles user requests related to CRUD operations for Songs and returns an HTTP status
 */
@RestController
@RequestMapping("/api/song")
public class SongController {

    private final SongService songService;
    private final ComposerService composerService;

    public SongController(SongService songService,
                          ComposerService composerService) {
        this.songService = songService;
        this.composerService = composerService;
    }

    @GetMapping
    public ResponseEntity<Page<SongDTO>> listAllSongs(Pageable pageable) {
        Page<Song> songs = songService.getAllSongs(pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(songs, pageable));
    }

    @PostMapping("/add")
    public ResponseEntity<SongDTO> addOrUpdateSong(@RequestBody SongDTO songDTO) {
        if (composerService.findComposerById(songDTO.getComposer().getId()).isEmpty()) {
            throw new NoEntityFoundException("Composer not found");
        }
        Song song = convertSongDTOToEntity(songDTO);
        song.setComposer(composerService.findComposerById(songDTO.getComposer().getId()).get());
        return ResponseEntity.ok(convertEntityToSongDTO(songService.saveSong(song)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        Optional<Song> song = songService.findSongById(id);
        if (song.isEmpty()) {
            throw new NoEntityFoundException("Song not found");
        }
        songService.deleteSong(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/title/{titleFragment}")
    public ResponseEntity<Page<SongDTO>> searchSongsByTitle(@PathVariable String titleFragment, Pageable pageable) {
        Page<Song> songs = songService.searchSongsByTitle(titleFragment, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(songs, pageable));
    }

    @GetMapping("/search/composer/{composerName}")
    public ResponseEntity<Page<SongDTO>> searchSongsByComposer(@PathVariable String composerName, Pageable pageable) {
        Page<Song> songs = songService.searchSongsByComposer(composerName, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(songs, pageable));
    }

    private static PageImpl<SongDTO> convertPageOfEntitiesToPageImplOfSongDTOs(Page<Song> songs, Pageable pageable) {
        List<SongDTO> songDTOs = new ArrayList<>();
        songs.forEach(song ->
                songDTOs.add(convertEntityToSongDTO(song)));
        return new PageImpl<>(songDTOs, pageable, songs.getTotalElements());
    }

    private static SongDTO convertEntityToSongDTO(Song song) {
        SongDTO songDTO = new SongDTO();
        BeanUtils.copyProperties(song, songDTO);
        ComposerDTO composerDTO = new ComposerDTO();
        BeanUtils.copyProperties(song.getComposer(), composerDTO);
        songDTO.setComposer(composerDTO);
        return songDTO;
    }

    private static Song convertSongDTOToEntity(SongDTO songDTO) {
        Song song = new Song();
        BeanUtils.copyProperties(songDTO, song);
        return song;
    }
}

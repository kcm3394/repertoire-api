package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.dtos.SongDTO;
import personal.kcm3394.repertoireapi.service.AppUserService;
import personal.kcm3394.repertoireapi.service.ComposerService;
import personal.kcm3394.repertoireapi.service.SongService;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<List<SongDTO>> listAllSongs() {
        List<Song> songs = songService.getAllSongs();
        List<SongDTO> songDTOs = new ArrayList<>();
        songs.forEach(song ->
                songDTOs.add(convertEntityToSongDTO(song)));
        return ResponseEntity.ok(songDTOs);
    }

    @PostMapping("/add")
    public ResponseEntity<SongDTO> addOrUpdateSong(@RequestBody SongDTO songDTO) {
        if (composerService.findComposerById(songDTO.getComposer().getId()) == null) {
            return ResponseEntity.badRequest().build();
        }
        Song song = convertSongDTOToEntity(songDTO);
        song.setComposer(composerService.findComposerById(songDTO.getComposer().getId()));
        return ResponseEntity.ok(convertEntityToSongDTO(songService.saveSong(song)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        Song song = songService.findSongById(id);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }
        songService.deleteSong(id);
        return ResponseEntity.ok().build();
    }

    private static SongDTO convertEntityToSongDTO(Song song) {
        SongDTO songDTO = new SongDTO();
        BeanUtils.copyProperties(song, songDTO);
        return songDTO;
    }

    private static Song convertSongDTOToEntity(SongDTO songDTO) {
        Song song = new Song();
        BeanUtils.copyProperties(songDTO, song);
        return song;
    }
}

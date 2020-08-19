package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.dtos.ComposerDTO;
import personal.kcm3394.repertoireapi.domain.dtos.SongDTO;
import personal.kcm3394.repertoireapi.service.AppUserService;
import personal.kcm3394.repertoireapi.service.SongService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles user requests related to CRUD operations for their repertoire(set of Songs) and returns an HTTP status
 */
@RestController
@RequestMapping("/api/repertoire")
public class RepertoireController {

    private final SongService songService;
    private final AppUserService appUserService;

    public RepertoireController(SongService songService, AppUserService appUserService) {
        this.songService = songService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<SongDTO>> getRepertoireList(Authentication auth) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        List<Song> repertoire = songService.findAllSongsInUserRepertoire(userId);
        List<SongDTO> songDTOs = new ArrayList<>();
        repertoire.forEach(song ->
                songDTOs.add(convertEntityToSongDTO(song)));
        return ResponseEntity.ok(songDTOs);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<SongDTO> addSongToRepertoire(Authentication auth, @PathVariable Long id) {
        Song song = songService.findSongById(id);
        if (song == null) {
            return ResponseEntity.badRequest().build();
        }

        AppUser appUser = appUserService.findUserByUsername(auth.getName());
        Set<Song> repertoire = appUser.getRepertoire();
        if (!repertoire.add(song)) {
            return ResponseEntity.badRequest().build();
        }

        repertoire.add(song);
        appUser.setRepertoire(repertoire);
        appUserService.saveOrUpdateUser(appUser);
        return ResponseEntity.ok(convertEntityToSongDTO(song));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSongFromRepertoire(Authentication auth, @PathVariable Long id) {
        Song song = songService.findSongById(id);
        if (song == null) {
            return ResponseEntity.badRequest().build();
        }

        AppUser appUser = appUserService.findUserByUsername(auth.getName());
        Set<Song> repertoire = appUser.getRepertoire();
        if (!repertoire.remove(song)) {
            return ResponseEntity.badRequest().build();
        }

        repertoire.remove(song);
        appUser.setRepertoire(repertoire);
        appUserService.saveOrUpdateUser(appUser);
        return ResponseEntity.ok().build();
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

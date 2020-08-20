package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.repertoireapi.domain.AppUser;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.dtos.ComposerDTO;
import personal.kcm3394.repertoireapi.domain.dtos.SongDTO;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Status;
import personal.kcm3394.repertoireapi.service.AppUserService;
import personal.kcm3394.repertoireapi.service.ComposerService;
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
    private final ComposerService composerService;

    public RepertoireController(SongService songService,
                                AppUserService appUserService,
                                ComposerService composerService) {
        this.songService = songService;
        this.appUserService = appUserService;
        this.composerService = composerService;
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

    @GetMapping("/byStatus/{status}")
    public ResponseEntity<List<SongDTO>> getRepertoireListByStatus(Authentication auth, @PathVariable Status status) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        List<Song> repertoireByStatus = songService.findAllSongsInUserRepertoireByStatus(userId, status);
        List<SongDTO> songDTOs = new ArrayList<>();
        repertoireByStatus.forEach(song ->
                songDTOs.add(convertEntityToSongDTO(song)));
        return ResponseEntity.ok(songDTOs);
    }

    @GetMapping("/byLanguage/{language}")
    public ResponseEntity<List<SongDTO>> getRepertoireListByLanguage(Authentication auth, @PathVariable Language language) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        List<Song> repertoireByLanguage = songService.findAllSongsInUserRepertoireByLanguage(userId, language);
        List<SongDTO> songDTOs = new ArrayList<>();
        repertoireByLanguage.forEach(song ->
                songDTOs.add(convertEntityToSongDTO(song)));
        return ResponseEntity.ok(songDTOs);
    }

    @GetMapping("/byComposer/{composerId}")
    public ResponseEntity<List<SongDTO>> getRepertoireListByComposer(Authentication auth, @PathVariable Long composerId) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Composer composer = composerService.findComposerById(composerId);
        if (composer == null) {
            return ResponseEntity.notFound().build();
        }

        List<Song> repertoireByComposer = songService.findAllSongsInUserRepertoireByComposer(userId, composerId);
        List<SongDTO> songDTOs = new ArrayList<>();
        repertoireByComposer.forEach(song ->
                songDTOs.add(convertEntityToSongDTO(song)));
        return ResponseEntity.ok(songDTOs);
    }

    @GetMapping("/byEpoch/{epoch}")
    public ResponseEntity<List<SongDTO>> getRepertoireListByEpoch(Authentication auth, @PathVariable Epoch epoch) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        List<Song> repertoireByEpoch = songService.findAllSongsInUserRepertoireByEpoch(userId, epoch);
        List<SongDTO> songDTOs = new ArrayList<>();
        repertoireByEpoch.forEach(song ->
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
}

package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import personal.kcm3394.repertoireapi.exceptions.NoEntityFoundException;
import personal.kcm3394.repertoireapi.exceptions.RepertoireException;
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
    public ResponseEntity<Page<SongDTO>> getRepertoireList(Authentication auth, Pageable pageable) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Page<Song> repertoire = songService.findAllSongsInUserRepertoire(userId, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(repertoire, pageable));
    }

    @GetMapping("/byStatus/{status}")
    public ResponseEntity<Page<SongDTO>> getRepertoireListByStatus(Authentication auth, @PathVariable Status status, Pageable pageable) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Page<Song> repertoireByStatus = songService.findAllSongsInUserRepertoireByStatus(userId, status, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(repertoireByStatus, pageable));
    }

    @GetMapping("/byLanguage/{language}")
    public ResponseEntity<Page<SongDTO>> getRepertoireListByLanguage(Authentication auth, @PathVariable Language language, Pageable pageable) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Page<Song> repertoireByLanguage = songService.findAllSongsInUserRepertoireByLanguage(userId, language, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(repertoireByLanguage, pageable));
    }

    @GetMapping("/byComposer/{composerId}")
    public ResponseEntity<Page<SongDTO>> getRepertoireListByComposer(Authentication auth, @PathVariable Long composerId, Pageable pageable) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Composer composer = composerService.findComposerById(composerId);
        if (composer == null) {
            throw new NoEntityFoundException("Composer not found");
        }

        Page<Song> repertoireByComposer = songService.findAllSongsInUserRepertoireByComposer(userId, composerId, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(repertoireByComposer, pageable));
    }

    @GetMapping("/byEpoch/{epoch}")
    public ResponseEntity<Page<SongDTO>> getRepertoireListByEpoch(Authentication auth, @PathVariable Epoch epoch, Pageable pageable) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Page<Song> repertoireByEpoch = songService.findAllSongsInUserRepertoireByEpoch(userId, epoch, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(repertoireByEpoch, pageable));
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<SongDTO> addSongToRepertoire(Authentication auth, @PathVariable Long id) {
        Song song = songService.findSongById(id);
        if (song == null) {
            throw new NoEntityFoundException("Song not found");
        }

        AppUser appUser = appUserService.findUserByUsername(auth.getName());
        Set<Song> repertoire = appUser.getRepertoire();
        if (!repertoire.add(song)) {
            throw new RepertoireException("Song is already in your repertoire");
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
            throw new NoEntityFoundException("Song not found");
        }

        AppUser appUser = appUserService.findUserByUsername(auth.getName());
        Set<Song> repertoire = appUser.getRepertoire();
        if (!repertoire.remove(song)) {
            throw new RepertoireException("Song is not in your repertoire");
        }

        repertoire.remove(song);
        appUser.setRepertoire(repertoire);
        appUserService.saveOrUpdateUser(appUser);
        return ResponseEntity.ok().build();
    }

    private static PageImpl<SongDTO> convertPageOfEntitiesToPageImplOfSongDTOs(Page<Song> songs, Pageable pageable) {
        List<SongDTO> songDTOs = new ArrayList<>();
        for (Song song : songs) {
            songDTOs.add(convertEntityToSongDTO(song));
        }
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
}

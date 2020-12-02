package personal.kcm3394.songcomposerservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.songcomposerservice.model.*;
import personal.kcm3394.songcomposerservice.model.dtos.ComposerDto;
import personal.kcm3394.songcomposerservice.model.dtos.RepertoireDto;
import personal.kcm3394.songcomposerservice.model.dtos.SongDto;
import personal.kcm3394.songcomposerservice.service.ComposerService;
import personal.kcm3394.songcomposerservice.service.RepertoireService;
import personal.kcm3394.songcomposerservice.service.SongService;

import java.util.*;

@RestController
@RequestMapping("/api/v2/repertoire")
@RequiredArgsConstructor
@Slf4j
public class RepertoireController {

    //todo methods should only be allowed by the user of the same id OR user with admin privileges, will change with security impl

    private final RepertoireService repertoireService;
    private final SongService songService;
    private final ComposerService composerService;

    @GetMapping("/repId/{repId}")
    public ResponseEntity<RepertoireDto> getRepertoireById(@PathVariable Long repId) {
        //todo admin only
        Optional<Repertoire> optionalRepertoire = repertoireService.findRepertoireById(repId);
        if (optionalRepertoire.isEmpty()) {
            //todo custom error response
            log.error("Repertoire by id " + repId + " not found");
            return ResponseEntity.notFound().build();
        }
        log.info("Finding repertoire " + repId);
        return ResponseEntity.ok(convertEntityToRepertoireDto(optionalRepertoire.get()));
    }

    @PutMapping("/{userId}/create-repertoire")
    public ResponseEntity<Void> createRepertoireForNewUser(@PathVariable Long userId) {
        //todo have this happen through event listener - user creation sends event and this service creates this, admin only
        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire != null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " already exists");
            return ResponseEntity.badRequest().build();
        }

        log.info("Creating empty repertoire for user " + userId);
        Repertoire newRepertoire = Repertoire.builder()
                .userId(userId)
                .repertoire(new HashSet<>())
                .build();
        repertoireService.saveRepertoire(newRepertoire);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/delete-repertoire")
    public ResponseEntity<Void> deleteRepertoireForDeletedUser(@PathVariable Long userId) {
        //todo have this happen through event listener - user deletion sends event and this service deletes it, admin only
        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire == null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " does not exist");
            return ResponseEntity.badRequest().build();
        }

        log.info("Deleting repertoire for user " + userId);
        repertoireService.deleteRepertoire(repertoire.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<SongDto>> getUserRepertoire(@PathVariable Long userId, Pageable pageable) {
        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire == null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        log.info("Finding repertoire for user " + userId);
        Long repertoireId = repertoire.getId();
        Page<Song> songs = songService.findAllSongsInRepertoire(repertoireId, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDtos(songs, pageable));
    }

    @GetMapping("/{userId}/language/{language}")
    public ResponseEntity<Page<SongDto>> getRepertoireByLanguage(@PathVariable Long userId, @PathVariable Language language, Pageable pageable) {
        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire == null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        log.info("Finding repertoire in language " + language.toString() + " for user " + userId);
        Long repertoireId = repertoire.getId();
        Page<Song> songs = songService.findAllSongsInRepertoireByLanguage(repertoireId, language, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDtos(songs, pageable));
    }

    @GetMapping("/{userId}/composer/{composerId}")
    public ResponseEntity<Page<SongDto>> getRepertoireByComposer(@PathVariable Long userId, @PathVariable Long composerId, Pageable pageable) {
        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire == null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        Optional<Composer> composer = composerService.findComposerById(composerId);
        if (composer.isEmpty()) {
            //todo custom error response
            log.error("Composer with id " + composerId + " not found");
            return ResponseEntity.notFound().build();
        }

        log.info("Finding repertoire by " + composer.get().getName() + " for user " + userId);
        Long repertoireId = repertoire.getId();
        Page<Song> songs = songService.findAllSongsInRepertoireByComposer(repertoireId, composerId, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDtos(songs, pageable));
    }

    @GetMapping("/{userId}/epoch/{epoch}")
    public ResponseEntity<Page<SongDto>> getRepertoireByEpoch(@PathVariable Long userId, @PathVariable Epoch epoch, Pageable pageable) {
        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire == null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        log.info("Finding repertoire from " + epoch.toString() + " period for user " + userId);
        Long repertoireId = repertoire.getId();
        Page<Song> songs = songService.findAllSongsInRepertoireByEpoch(repertoireId, epoch, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDtos(songs, pageable));
    }

    @PostMapping("/{userId}/add-song/{songId}")
    public ResponseEntity<SongDto> addSongToRepertoire(@PathVariable Long userId, @PathVariable Long songId) {
        Optional<Song> optionalSong = songService.findSongById(songId);
        if (optionalSong.isEmpty()) {
            //todo custom error response
            log.error("Song with id " + songId + " not found");
            return ResponseEntity.notFound().build();
        }
        Song song = optionalSong.get();

        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire == null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        if (repertoire.getRepertoire().contains(song)) {
            //todo custom error response
            log.error("Repertoire already contains this song");
            return ResponseEntity.badRequest().build();
        }

        log.info("Adding song " + song.getTitle() + " to repertoire for user " + userId);
        repertoire.getRepertoire().add(song);
        repertoireService.saveRepertoire(repertoire);
        return ResponseEntity.ok(convertEntityToSongDto(song));
    }

    @DeleteMapping("/{userId}/delete-song/{songId}")
    public ResponseEntity<Void> deleteSongFromRepertoire(@PathVariable Long userId, @PathVariable Long songId) {
        Optional<Song> optionalSong = songService.findSongById(songId);
        if (optionalSong.isEmpty()) {
            //todo custom error response
            return ResponseEntity.notFound().build();
        }
        Song song = optionalSong.get();

        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire == null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        if (!repertoire.getRepertoire().contains(song)) {
            //todo custom error response
            log.error("Repertoire does not contain this song");
            return ResponseEntity.badRequest().build();
        }

        log.info("Deleting song " + song.getTitle() + " from repertoire for user " + userId);
        repertoire.getRepertoire().remove(song);
        repertoireService.saveRepertoire(repertoire);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/reset")
    public ResponseEntity<Void> resetRepertoireToEmpty(@PathVariable Long userId) {
        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire == null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        log.info("Resetting repertoire for user with id " + userId);
        repertoire.getRepertoire().clear();
        repertoireService.saveRepertoire(repertoire);
        return ResponseEntity.ok().build();
    }

    private static PageImpl<SongDto> convertPageOfEntitiesToPageImplOfSongDtos(Page<Song> songs, Pageable pageable) {
        List<SongDto> songDtos = new ArrayList<>();
        songs.forEach(song ->
                songDtos.add(convertEntityToSongDto(song)));
        return new PageImpl<>(songDtos, pageable, songs.getTotalElements());
    }

    private static SongDto convertEntityToSongDto(Song song) {
        SongDto songDto = new SongDto();
        BeanUtils.copyProperties(song, songDto);
        ComposerDto composerDto = new ComposerDto();
        BeanUtils.copyProperties(song.getComposer(), composerDto);
        songDto.setComposer(composerDto);
        return songDto;
    }

    private static RepertoireDto convertEntityToRepertoireDto(Repertoire repertoire) {
        RepertoireDto repertoireDto = new RepertoireDto();
        BeanUtils.copyProperties(repertoire, repertoireDto);
        Set<Song> songs = repertoire.getRepertoire();
        List<SongDto> songDtos = new ArrayList<>();
        songs.forEach(song ->
                songDtos.add(convertEntityToSongDto(song)));
        repertoireDto.setRepertoire(songDtos);
        return repertoireDto;
    }
}

package personal.kcm3394.songcomposerservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.songcomposerservice.domain.Song;
import personal.kcm3394.songcomposerservice.domain.dtos.ComposerDto;
import personal.kcm3394.songcomposerservice.domain.dtos.SongDto;
import personal.kcm3394.songcomposerservice.service.ComposerService;
import personal.kcm3394.songcomposerservice.service.SongService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/songs")
@RequiredArgsConstructor
@Slf4j
public class SongController {

    private final SongService songService;
    private final ComposerService composerService;

    @GetMapping
    public ResponseEntity<Page<SongDto>> listAllSongs(Pageable pageable) {
        log.info("Finding all songs");
        Page<Song> songs = songService.getAllSongs(pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDtos(songs, pageable));
    }

    @PostMapping("/add")
    public ResponseEntity<SongDto> addOrUpdateSong(@RequestBody SongDto songDto) {
        if (composerService.findComposerById(songDto.getComposer().getId()).isEmpty()) {
            //todo custom error response
            log.error("Composer not found when adding song: " + songDto.getTitle());
            return ResponseEntity.notFound().build();
        }
        log.info("Adding/Updating song: " + songDto.getTitle());
        Song song = convertSongDtoToEntity(songDto);
        song.setComposer(composerService.findComposerById(songDto.getComposer().getId()).get());
        return ResponseEntity.ok(convertEntityToSongDto(songService.saveSong(song)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        Optional<Song> song = songService.findSongById(id);
        if (song.isEmpty()) {
            //todo custom error response
            log.error("Song not found with id: " + id);
            return ResponseEntity.notFound().build();
        }
        log.info("Deleting song with id: " + id);
        songService.deleteSong(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/title/{titleFragment}")
    public ResponseEntity<Page<SongDto>> searchSongsByTitle(@PathVariable String titleFragment, Pageable pageable) {
        log.info("Searching for songs with title like: " + titleFragment);
        Page<Song> songs = songService.searchSongsByTitle(titleFragment, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDtos(songs, pageable));
    }

    @GetMapping("/search/composer/{composerName}")
    public ResponseEntity<Page<SongDto>> searchSongsByComposer(@PathVariable String composerName, Pageable pageable) {
        log.info("Searching for songs with composer: " + composerName);
        Page<Song> songs = songService.searchSongsByComposer(composerName, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDtos(songs, pageable));
    }

    private static PageImpl<SongDto> convertPageOfEntitiesToPageImplOfSongDtos(Page<Song> songs, Pageable pageable) {
        List<SongDto> songDTOs = new ArrayList<>();
        songs.forEach(song ->
                songDTOs.add(convertEntityToSongDto(song)));
        return new PageImpl<>(songDTOs, pageable, songs.getTotalElements());
    }

    private static SongDto convertEntityToSongDto(Song song) {
        SongDto songDto = new SongDto();
        BeanUtils.copyProperties(song, songDto);
        ComposerDto composerDto = new ComposerDto();
        BeanUtils.copyProperties(song.getComposer(), composerDto);
        songDto.setComposer(composerDto);
        return songDto;
    }

    private static Song convertSongDtoToEntity(SongDto songDto) {
        Song song = new Song();
        BeanUtils.copyProperties(songDto, song);
        return song;
    }
}

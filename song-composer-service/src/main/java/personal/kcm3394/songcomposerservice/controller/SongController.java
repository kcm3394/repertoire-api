package personal.kcm3394.songcomposerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.songcomposerservice.domain.Song;
import personal.kcm3394.songcomposerservice.domain.dtos.ComposerDto;
import personal.kcm3394.songcomposerservice.domain.dtos.SongDto;
import personal.kcm3394.songcomposerservice.service.SongService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping
    public ResponseEntity<Page<SongDto>> listAllSongs(Pageable pageable) {
        Page<Song> songs = songService.getAllSongs(pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(songs, pageable));
    }

    @PostMapping("/add")
    public ResponseEntity<SongDto> addOrUpdateSong(@RequestBody SongDto songDto) {
        //todo implement
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        Optional<Song> song = songService.findSongById(id);
        if (song.isEmpty()) {
            //todo custom error response
            return ResponseEntity.notFound().build();
        }
        songService.deleteSong(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/title/{titleFragment}")
    public ResponseEntity<Page<SongDto>> searchSongsByTitle(@PathVariable String titleFragment, Pageable pageable) {
        Page<Song> songs = songService.searchSongsByTitle(titleFragment, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(songs, pageable));
    }

    @GetMapping("/search/composer/{composerName}")
    public ResponseEntity<Page<SongDto>> searchSongsByComposer(@PathVariable String composerName, Pageable pageable) {
        Page<Song> songs = songService.searchSongsByComposer(composerName, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfSongDTOs(songs, pageable));
    }

    private static PageImpl<SongDto> convertPageOfEntitiesToPageImplOfSongDTOs(Page<Song> songs, Pageable pageable) {
        List<SongDto> songDTOs = new ArrayList<>();
        songs.forEach(song ->
                songDTOs.add(convertEntityToSongDTO(song)));
        return new PageImpl<>(songDTOs, pageable, songs.getTotalElements());
    }

    private static SongDto convertEntityToSongDTO(Song song) {
        SongDto songDto = new SongDto();
        BeanUtils.copyProperties(song, songDto);
        ComposerDto composerDto = new ComposerDto();
        BeanUtils.copyProperties(song.getComposer(), composerDto);
        songDto.setComposer(composerDto);
        return songDto;
    }
}

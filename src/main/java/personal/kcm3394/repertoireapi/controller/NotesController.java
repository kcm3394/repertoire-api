package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.repertoireapi.domain.Notes;
import personal.kcm3394.repertoireapi.domain.dtos.NotesDTO;
import personal.kcm3394.repertoireapi.exceptions.NoEntityFoundException;
import personal.kcm3394.repertoireapi.service.AppUserService;
import personal.kcm3394.repertoireapi.service.NotesService;
import personal.kcm3394.repertoireapi.service.SongService;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

    private final NotesService notesService;
    private final SongService songService;
    private final AppUserService appUserService;

    public NotesController(NotesService notesService,
                           SongService songService,
                           AppUserService appUserService) {
        this.notesService = notesService;
        this.songService = songService;
        this.appUserService = appUserService;
    }

    @GetMapping("/song/{songId}")
    public ResponseEntity<NotesDTO> getNotesForSong(@PathVariable Long songId, Authentication auth) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Notes notes = notesService.getNotesBySongIdAndUserId(userId, songId);
        if (notes == null) {
            throw new NoEntityFoundException("Notes for song not found");
        }

        return ResponseEntity.ok(convertEntityToNotesDTO(notes));
    }

    @PostMapping("/add/{songId}")
    public ResponseEntity<NotesDTO> addOrUpdateNotesToSong(@PathVariable Long songId,
                                                @RequestBody NotesDTO notesDTO,
                                                Authentication auth) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Notes notes = convertNotesDTOToEntity(notesDTO);
        if (notesService.getNotesBySongIdAndUserId(userId, songId) == null) {
            if (appUserService.findUserById(userId).isEmpty()) {
                throw new NoEntityFoundException("User not found");
            }
            if (songService.findSongById(songId).isEmpty()) {
                throw new NoEntityFoundException("Song not found");
            }
            notes.setUser(appUserService.findUserById(userId).get());
            notes.setSong(songService.findSongById(songId).get());
        }
        Notes savedNotes = notesService.saveNote(notes);
        return ResponseEntity.ok(convertEntityToNotesDTO(savedNotes));
    }

    @DeleteMapping("/delete/{songId}")
    public ResponseEntity<Void> deleteNotesFromSong(@PathVariable Long songId, Authentication auth) {
        Long userId = appUserService.findUserByUsername(auth.getName()).getId();
        Notes notes = notesService.getNotesBySongIdAndUserId(userId, songId);
        if (notes == null) {
            throw new NoEntityFoundException("Notes for song not found");
        }

        notesService.deleteNotes(notes.getId());
        return ResponseEntity.ok().build();
    }

    private static NotesDTO convertEntityToNotesDTO(Notes notes) {
        NotesDTO notesDTO = new NotesDTO();
        BeanUtils.copyProperties(notes, notesDTO);
        notesDTO.setSongTitle(notes.getSong().getTitle());
        notesDTO.setComposerName(notes.getSong().getComposer().getName());
        return notesDTO;
    }

    private static Notes convertNotesDTOToEntity(NotesDTO notesDTO) {
        Notes notes = new Notes();
        BeanUtils.copyProperties(notesDTO, notes);
        return notes;
    }
}

package personal.kcm3394.repertoireapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.kcm3394.repertoireapi.domain.Notes;
import personal.kcm3394.repertoireapi.repository.NotesRepository;

/**
 * Makes calls to the database layer related to CRUD operations for Notes
 */
@Service
@Transactional
public class NotesService {

    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public Notes saveNote(Notes notes) {
        return notesRepository.save(notes);
    }

    public Notes findNotesById(Long notesId) {
        return notesRepository.findById(notesId).orElse(null);
    }

    public void deleteNotes(Long notesId) {
        notesRepository.deleteById(notesId);
    }

    public Notes getNotesBySongIdAndUserId(Long userId, Long songId) {
        return notesRepository.findByUser_IdAndSong_Id(userId, songId);
    }
}

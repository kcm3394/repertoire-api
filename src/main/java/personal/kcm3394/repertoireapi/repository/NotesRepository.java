package personal.kcm3394.repertoireapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.kcm3394.repertoireapi.domain.Notes;

/**
 * Database connection layer for CRUD operations on Notes
 */
@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {

    Notes findByUser_IdAndSong_Id(Long userId, Long songId);
}

package personal.kcm3394.repertoireapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.kcm3394.repertoireapi.domain.Song;

import java.util.List;

/**
 * Database connection layer for CRUD operations on Songs
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s JOIN AppUser u WHERE u.id = :userId")
    List<Song> findAllByUserId(Long userId);
}

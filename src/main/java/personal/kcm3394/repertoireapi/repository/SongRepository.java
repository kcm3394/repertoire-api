package personal.kcm3394.repertoireapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.kcm3394.repertoireapi.domain.Song;

import java.util.List;

/**
 * Database connection layer for CRUD operations on Songs
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s, u FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId")
    List<Song> findAllByAppUser_Id(@Param("userId") Long userId);

    List<Song> findAllByTitleContaining(String titleFragment);

    @Query("SELECT s FROM Song s JOIN s.composer c WHERE c.name LIKE :pattern")
    List<Song> findAllByComposer_NameContains(String pattern);
}

package personal.kcm3394.repertoireapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Status;

import java.util.List;

/**
 * Database connection layer for CRUD operations on Songs
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s, u FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId ORDER BY s.title ASC")
    List<Song> findAllSongsInRepertoireByAppUser_Id(Long userId);

    List<Song> findAllByTitleContainingOrderByTitle(String titleFragment);

    @Query("SELECT s FROM Song s JOIN s.composer c WHERE c.name LIKE :pattern ORDER BY s.title ASC")
    List<Song> findAllByComposer_NameContains(String pattern);

    @Query("SELECT s, n FROM Notes n JOIN n.song s WHERE n.user.id = :userId AND n.status = :status")
    List<Song> findAllSongsInRepertoireByUserWithStatus(Long userId, Status status);

    @Query("SELECT s, u FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId AND s.language = :language")
    List<Song> findAllSongsInRepertoireByUserByLanguage(Long userId, Language language);

    @Query("SELECT s, u FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId AND s.composer.id = :composerId")
    List<Song> findAllSongsInRepertoireByUserByComposer(Long userId, Long composerId);

    @Query("SELECT s, u FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId AND s.composer.epoch = :epoch")
    List<Song> findAllSongsInRepertoireByUserByEpoch(Long userId, Epoch epoch);
}

//https://stackoverflow.com/questions/17242408/spring-query-annotation-with-enum-parameter
//https://stackoverflow.com/questions/44460394/can-i-use-enum-parameter-into-jparepository-nativequery
//https://stackoverflow.com/questions/8217144/problems-with-making-a-query-when-using-enum-in-entity
package personal.kcm3394.repertoireapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Status;

/**
 * Database connection layer for CRUD operations on Songs
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId")
    Page<Song> findAllSongsInRepertoireByAppUser_Id(Long userId, Pageable pageable);

    Page<Song> findAllByTitleContainingOrderByTitle(String titleFragment, Pageable pageable);

    @Query("SELECT s FROM Song s JOIN s.composer c WHERE c.name LIKE :pattern ORDER BY s.title ASC")
    Page<Song> findAllByComposer_NameContains(String pattern, Pageable pageable);

    @Query("SELECT s FROM Notes n JOIN n.song s WHERE n.user.id = :userId AND n.status = :status")
    Page<Song> findAllSongsInRepertoireByUserWithStatus(Long userId, Status status, Pageable pageable);

    @Query("SELECT s FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId AND s.language = :language")
    Page<Song> findAllSongsInRepertoireByUserByLanguage(Long userId, Language language, Pageable pageable);

    @Query("SELECT s FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId AND s.composer.id = :composerId")
    Page<Song> findAllSongsInRepertoireByUserByComposer(Long userId, Long composerId, Pageable pageable);

    @Query("SELECT s FROM AppUser u JOIN u.repertoire s WHERE u.id = :userId AND s.composer.epoch = :epoch")
    Page<Song> findAllSongsInRepertoireByUserByEpoch(Long userId, Epoch epoch, Pageable pageable);
}

//https://stackoverflow.com/questions/17242408/spring-query-annotation-with-enum-parameter
//https://stackoverflow.com/questions/44460394/can-i-use-enum-parameter-into-jparepository-nativequery
//https://stackoverflow.com/questions/8217144/problems-with-making-a-query-when-using-enum-in-entity

//https://www.baeldung.com/jpa-join-types
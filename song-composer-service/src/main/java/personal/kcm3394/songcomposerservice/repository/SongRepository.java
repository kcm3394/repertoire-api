package personal.kcm3394.songcomposerservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.kcm3394.songcomposerservice.model.Epoch;
import personal.kcm3394.songcomposerservice.model.Language;
import personal.kcm3394.songcomposerservice.model.Song;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s JOIN s.composer c WHERE s.title = :title AND c.id = :composerId")
    Song findByTitleAndComposerId(String title, Long composerId);

    Page<Song> findAllByTitleContainingOrderByTitle(String titleFragment, Pageable pageable);

    @Query("SELECT s FROM Song s JOIN s.composer c WHERE c.name LIKE :pattern ORDER BY s.title ASC")
    Page<Song> findAllByComposer_NameContains(String pattern, Pageable pageable);

    @Query("SELECT s FROM Repertoire r JOIN r.repertoire s WHERE r.id = :repId")
    Page<Song> findAllSongsInRepertoire(Long repId, Pageable pageable);

    @Query("SELECT s FROM Repertoire r JOIN r.repertoire s WHERE r.id = :repId AND s.language = :language")
    Page<Song> findAllSongsInRepertoireByLanguage(Long repId, Language language, Pageable pageable);

    @Query("SELECT s FROM Repertoire r JOIN r.repertoire s WHERE r.id = :repId AND s.composer.id = :composerId")
    Page<Song> findAllSongsInRepertoireByComposer(Long repId, Long composerId, Pageable pageable);

    @Query("SELECT s FROM Repertoire r JOIN r.repertoire s WHERE r.id = :repId AND s.composer.epoch = :epoch")
    Page<Song> findAllSongsInRepertoireByEpoch(Long repId, Epoch epoch, Pageable pageable);
}

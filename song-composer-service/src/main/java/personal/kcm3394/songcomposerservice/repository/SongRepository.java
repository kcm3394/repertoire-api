package personal.kcm3394.songcomposerservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.kcm3394.songcomposerservice.domain.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Page<Song> findAllByTitleContainingOrderByTitle(String titleFragment, Pageable pageable);

    @Query("SELECT s FROM Song s JOIN s.composer c WHERE c.name LIKE :pattern ORDER BY s.title ASC")
    Page<Song> findAllByComposer_NameContains(String pattern, Pageable pageable);
}

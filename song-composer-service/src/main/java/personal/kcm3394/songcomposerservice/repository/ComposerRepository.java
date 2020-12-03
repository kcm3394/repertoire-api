package personal.kcm3394.songcomposerservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.kcm3394.songcomposerservice.model.Composer;
import personal.kcm3394.songcomposerservice.model.Epoch;

@Repository
public interface ComposerRepository extends JpaRepository<Composer, Long> {

    Composer findByNameAndEpoch(String name, Epoch epoch);

    Page<Composer> findAllByNameContainingOrderByName(String nameFragment, Pageable pageable);

    Page<Composer> findAllByEpochOrderByName(Epoch epoch, Pageable pageable);

    @Query("SELECT c FROM Composer c JOIN c.compositions s WHERE s.title LIKE :pattern ORDER BY c.name ASC")
    Page<Composer> findAllByCompositions_TitleContains(String pattern, Pageable pageable);
}

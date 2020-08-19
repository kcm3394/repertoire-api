package personal.kcm3394.repertoireapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;

import java.util.List;

/**
 * Database connection layer for CRUD operations on Composers
 */
@Repository
public interface ComposerRepository extends JpaRepository<Composer, Long> {

    List<Composer> findAllByNameContaining(String nameFragment);

    List<Composer> findAllByEpoch(Epoch epoch);

    @Query("SELECT c FROM Composer c JOIN c.compositions s WHERE s.title LIKE :pattern")
    List<Composer> findAllByCompositions_TitleContains(String pattern);
}

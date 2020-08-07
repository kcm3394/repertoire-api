package personal.kcm3394.repertoireapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.kcm3394.repertoireapi.domain.Composer;

/**
 * Database connection layer for CRUD operations on Composers
 */
@Repository
public interface ComposerRepository extends JpaRepository<Composer, Long> {
}

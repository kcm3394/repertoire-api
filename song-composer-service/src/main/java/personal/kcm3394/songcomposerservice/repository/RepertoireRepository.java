package personal.kcm3394.songcomposerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.kcm3394.songcomposerservice.model.Repertoire;

@Repository
public interface RepertoireRepository extends JpaRepository<Repertoire, Long> {

    Repertoire findByUserId(Long userId);
}

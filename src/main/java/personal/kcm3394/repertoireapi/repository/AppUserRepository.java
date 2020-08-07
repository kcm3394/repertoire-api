package personal.kcm3394.repertoireapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.kcm3394.repertoireapi.domain.AppUser;

/**
 * Database connection layer for CRUD operations on AppUsers
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findAppUserByUsername(String username);
}

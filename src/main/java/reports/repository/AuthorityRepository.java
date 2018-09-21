package reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findOneByName(Authority.UserRoleName name);
}

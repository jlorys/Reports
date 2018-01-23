package reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	AppUser findOneByUsername(String username);
	AppUser findTop1ByOrderByIdDesc();
}

package reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reports.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	public AppUser findOneByUsername(String username);
	public AppUser findTop1ByOrderByIdDesc();
}

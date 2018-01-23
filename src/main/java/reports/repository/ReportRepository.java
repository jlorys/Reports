package reports.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reports.domain.AppUser;
import reports.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
	
	Report findTop1ByOrderByIdDesc();
	
	String FIND_FILES = "select id, description, extension, name from report";

	@Query(nativeQuery = true, value=FIND_FILES)
	List<Object> findAllWithoutFile();
	
	List<NoFile> findAllByOrderByIdAsc();
	
	List<NoFile> findByOwner(AppUser owner);

}

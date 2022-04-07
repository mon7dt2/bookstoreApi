package mon7.project.bookstore.admin.dao;

import mon7.project.bookstore.admin.models.ApplicationVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationVersionRepository extends JpaRepository<ApplicationVersion,String> {
}

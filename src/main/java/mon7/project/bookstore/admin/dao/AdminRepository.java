package mon7.project.bookstore.admin.dao;

import mon7.project.bookstore.admin.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,String>{

}

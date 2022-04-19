package mon7.project.bookstore.staff.dao;

import mon7.project.bookstore.staff.models.data.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, String> {
    Staff findByAccount_Id(String accountID);
}

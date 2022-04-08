package mon7.project.bookstore.customer.dao;

import mon7.project.bookstore.customer.models.data.Customer;
import mon7.project.bookstore.customer.models.view.CustomerView;
import mon7.project.bookstore.customer.models.view.HeaderProfile;
import mon7.project.bookstore.customer.models.view.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByUser_Id(String userID);
    @Query("select new mon7.project.bookstore.customer.models.view.HeaderProfile(" +
            "c.fullName," +
            "c.avatarUrl" +
            ") from Customer c where c.id = ?1")
    HeaderProfile getHeaderProfile(String id);

    @Query("select new mon7.project.bookstore.customer.models.view.Profile(c)" +
            "from Customer c where c.id = ?1")
    Profile getProfile(String customerID);

    @Query("select new mon7.project.bookstore.customer.models.view.CustomerView(c)" +
            "from Customer c")
    List<CustomerView> getAllCustomers();
}

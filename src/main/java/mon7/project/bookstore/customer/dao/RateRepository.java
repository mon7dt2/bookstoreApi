package mon7.project.bookstore.customer.dao;

import mon7.project.bookstore.customer.models.data.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate,String> {
}

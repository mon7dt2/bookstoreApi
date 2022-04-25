package mon7.project.bookstore.order.dao;

import mon7.project.bookstore.order.model.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
}

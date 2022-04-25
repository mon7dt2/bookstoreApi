package mon7.project.bookstore.order.dao;

import mon7.project.bookstore.order.model.data.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}

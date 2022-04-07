package mon7.project.bookstore.customer.dao;

import mon7.project.bookstore.customer.models.data.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,String> {
}

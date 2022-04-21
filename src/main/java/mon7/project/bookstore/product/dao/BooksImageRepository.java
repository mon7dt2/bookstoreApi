package mon7.project.bookstore.product.dao;

import mon7.project.bookstore.product.model.data.BooksImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksImageRepository extends JpaRepository<BooksImage, String> {

}

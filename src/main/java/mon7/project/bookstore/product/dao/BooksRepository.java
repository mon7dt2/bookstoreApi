package mon7.project.bookstore.product.dao;

import mon7.project.bookstore.category.model.Category;
import mon7.project.bookstore.product.model.data.Books;
import mon7.project.bookstore.product.model.view.BookPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Books, String> {

    @Query("SELECT new mon7.project.bookstore.product.model.view.BookPreview(b.id, b.displayName, b.avatarUrl, b.price, b.quantity) " +
        " FROM Books b WHERE b.isDeleted = 0")
    Page<BookPreview> getBookPreview(Pageable pageable);

    @Query("SELECT new mon7.project.bookstore.product.model.view.BookPreview(b.id, b.displayName, b.avatarUrl, b.price, b.quantity) " +
            " FROM Books b WHERE b.isDeleted = 0 AND b.category = :category")
    Page<BookPreview> getBookPreviewByCategory(Pageable pageable,@Param("category") Category categoryID);
}

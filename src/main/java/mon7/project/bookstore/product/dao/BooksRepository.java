package mon7.project.bookstore.product.dao;

import mon7.project.bookstore.category.model.Category;
import mon7.project.bookstore.product.model.data.Books;
import mon7.project.bookstore.product.model.view.Book;
import mon7.project.bookstore.product.model.view.BookPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BooksRepository extends JpaRepository<Books, String> {

    @Query( value = "SELECT * " +
        " FROM books WHERE isDeleted = 0",
            countQuery = "SELECT count(*) FROM books WHERE isDeleted = 0",
            nativeQuery = true)
    Page<Books> getBookPreview(Pageable pageable);

    @Query(value = "SELECT * " +
            " FROM books WHERE isDeleted = 0 AND categoryID = :category" ,
            countQuery = "SELECT count(*) FROM books WHERE isDeleted = 0"
            ,nativeQuery = true)
    Page<Books> getBookPreviewByCategory(Pageable pageable,@Param("category") Category categoryID);

    @Query(value = "SELECT categoryID, count(id) FROM books WHERE isDeleted != 1 GROUP BY categoryID", nativeQuery = true)
    List<Object[]> sumByCategory();
}

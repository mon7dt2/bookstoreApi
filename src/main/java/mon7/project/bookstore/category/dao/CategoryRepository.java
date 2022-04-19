package mon7.project.bookstore.category.dao;

import mon7.project.bookstore.category.model.Category;
import mon7.project.bookstore.category.model.view.CategoryPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query( value = "select * from Category c where c.isDeleted = 0", nativeQuery = true)
    Page<Category> getAllCategories(Pageable pageable);

    Category findByDisplayName(String displayName);
}

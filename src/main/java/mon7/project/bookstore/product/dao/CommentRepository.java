package mon7.project.bookstore.product.dao;

import mon7.project.bookstore.product.model.data.Comment;
import mon7.project.bookstore.product.model.view.CommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT new mon7.project.bookstore.product.model.view.CommentView(c) "+
        " FROM Comment c WHERE c.isDeleted = 0",
    countQuery = "SELECT count(*) FROM Comment c WHERE c.isDeleted = 0")
    Page<CommentView> getAllComments(Pageable pageable);
}

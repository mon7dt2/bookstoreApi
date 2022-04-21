package mon7.project.bookstore.provider.dao;

import mon7.project.bookstore.provider.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    @Query(value = "SELECT * FROM provider WHERE isDeleted = 0 ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(*) FROM provider WHERE isDeleted = 0",
            nativeQuery = true)
    Page<Provider> getAllProvider(Pageable pageable);

    Provider findByDisplayName(String displayName);
}

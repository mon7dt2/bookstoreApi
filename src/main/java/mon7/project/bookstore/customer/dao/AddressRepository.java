package mon7.project.bookstore.customer.dao;

import mon7.project.bookstore.customer.models.data.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    @Query(value = "SELECT * FROM address WHERE isDeleted = 0 AND customerID = ?1 ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(*) FROM provider WHERE isDeleted = 0",
            nativeQuery = true)
    Page<Address> getAllAddressByCustomer(String customerID, Pageable pageable);

    Address findByAddress(String address);

    @Modifying
    @Transactional
    @Query(value = "UPDATE address SET isDefault = 0 WHERE isDeleted = 0 AND customerID = :customerID",
            nativeQuery = true)
    void setAllAddressToUnDefault(@Param("customerID") String customerID);
}

package mon7.project.bookstore.auth.dao;

import mon7.project.bookstore.auth.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRespository extends JpaRepository<Account,String> {
    Account findByUsernameAndPassword(String username, String password);

    Account findByUsername(String username);

    @Query("select u.isActivated from Account u where u.username = :username and u.role = :role")
    int isActivated(@Param("username") String username, @Param("role") String role);

    @Modifying
    @Query("update Account u set u.isActivated  = ?1 where u.username = ?2")
    @Transactional
    void activeAccount(int isActivated, String username);

    @Modifying
    @Query("update Account a set a.password = ?1 where a.username = ?2")
    @Transactional
    void changePassword(String newPassword, String username);
}

package restapi.banking.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import restapi.banking.app.model.Account;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    List<Account> findAccountsByUserId(@Param("userId") UUID userId);


    Account findByIban(String iban);

}

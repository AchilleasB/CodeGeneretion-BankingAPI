package restapi.banking.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restapi.banking.app.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    //will occur errors
    //Optional<Account> getAccountByIban(String iban);
    Account getReferenceById(String iban);
}

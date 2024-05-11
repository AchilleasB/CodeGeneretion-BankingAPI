package restapi.banking.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restapi.banking.app.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}

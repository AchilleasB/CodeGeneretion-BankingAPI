package restapi.banking.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import restapi.banking.app.model.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    // Find accounts by user ID
    List<Account> findAccountByUserId(UUID userId);

}

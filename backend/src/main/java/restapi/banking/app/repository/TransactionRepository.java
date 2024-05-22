package restapi.banking.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import restapi.banking.app.model.Transaction;
import restapi.banking.app.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * this method is giving the sum of all the transactions created by provided user at the provided date
     * @param userId is the user identifier
     * @param date is the date when the transactions were created in LocalDate format
     * @return the sum of the transactions created in the given date
     */
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.accountFrom.user.id = :userId AND t.timestamp = :date")
    BigDecimal TotalTransferred(@Param("userId") UUID userId, @Param("date") LocalDate date);
}

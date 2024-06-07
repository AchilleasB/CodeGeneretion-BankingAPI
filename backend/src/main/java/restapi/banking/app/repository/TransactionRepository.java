package restapi.banking.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import restapi.banking.app.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * This method returns the sum of all the transactions created by the provided user within the provided period of time.
     * @param userId is the user identifier
     * @param timeFrom is the time from which we want to see transactions
     * @param timeTo is the time to which we want to see transactions
     * @return the sum of the transactions created in the given period of time
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.accountFrom.user.id = :userId AND t.timestamp BETWEEN :timeFrom AND :timeTo")
    BigDecimal totalTransferred(@Param("userId") UUID userId, @Param("timeFrom") LocalDateTime timeFrom, @Param("timeTo") LocalDateTime timeTo);


    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId ORDER BY t.timestamp DESC")
    Page<Transaction> findByUserId(@Param("userId") UUID userId, Pageable pageable);
}

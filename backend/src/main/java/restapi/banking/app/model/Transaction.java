package restapi.banking.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="transaction_type")
    private TransactionType type;
    
    @Column(name="amount")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from", referencedColumnName = "id")
    @JsonIgnoreProperties("transactions")
    private Account accountFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acount_to", referencedColumnName = "id")
    @JsonIgnoreProperties("transactions")
    private Account accountTo;

    @Column(name="message")
    private String message;

    @Column(name="timestamp")
    private LocalDate timestamp;

}

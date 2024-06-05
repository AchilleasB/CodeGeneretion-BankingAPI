package restapi.banking.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Column(name="personal")
    private Boolean personal;

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
    private LocalDateTime timestamp;

    @Column(name="user_id")
    private UUID userId;
}

package restapi.banking.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "iban", unique = true)
    private String iban;

    @Column(name = "balance")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "opening_date")
    private LocalDate openingDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @JsonBackReference // Prevents cyclic serialization
//    private User user;

    @Column(name = "absolute_limit")
    private BigDecimal absoluteLimit;

    @Column(name = "transaction_limit")
    private BigDecimal transactionLimit;

    @Column(name = "active")
    private boolean active;

    @Column(name = "user_id")
    private UUID userId;
}

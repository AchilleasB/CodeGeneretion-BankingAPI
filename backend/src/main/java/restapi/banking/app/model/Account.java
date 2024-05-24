package restapi.banking.app.model;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name= "accounts")
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

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties("accounts")
    private User user;


    @Column(name = "absolute_limit")
    private BigDecimal absoluteLimit;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "transaction_limit")
    private BigDecimal transactionLimit;

    @Column(name = "active")
    private boolean active;



}

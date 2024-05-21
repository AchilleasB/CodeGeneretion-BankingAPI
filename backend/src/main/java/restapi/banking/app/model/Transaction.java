package restapi.banking.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
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

    @Column(name="amount")
    private BigDecimal amount; //BigDecimal over double or float due to precision level

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from", referencedColumnName = "id")
    private Account AccountFrom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to", referencedColumnName = "id")
    private Account AccountTo;

    @Column(name="message")
    private String message;

    @Column(name="timestamp")
    private LocalDateTime timestamp;

    //done: change ibans to Accounts

}

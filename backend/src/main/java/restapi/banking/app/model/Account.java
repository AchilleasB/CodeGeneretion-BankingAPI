package restapi.banking.app.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name= "accounts")
public class Account {
    @Id
    @GenericGenerator(name = "IBANGenerator", strategy = "restapi.banking.app.utilities.IBANGenerator")
    @GeneratedValue(generator = "IBANGenerator")
    private String iban;
    private AccountType accountType;
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.PENDING;
    @Builder.Default
    private double balance = 0.0;
    @Builder.Default
    private LocalDate openingDate = LocalDate.now();
    @Builder.Default
    private double dailyLimit = 0.00; // for ex, max daily withdrawal limit is 200 euros
    @Builder.Default
    private double absoluteLimit = 0.00;// for ex, they can not go below 0 euros


}

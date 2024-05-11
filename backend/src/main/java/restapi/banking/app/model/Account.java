package restapi.banking.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restapi.banking.app.utilities.IBANGenerator;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name= "accounts")
public class Account {
    @Id
    private String iban;
    private AccountType accountType;
    private double balance;
    private LocalDate openingDate;
    private double dailyLimit; // for ex, max daily withdrawal limit is 200 euros
    private double absoluteLimit;// for ex, they can not go below 0 euros



}

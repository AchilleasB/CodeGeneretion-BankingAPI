package restapi.banking.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "id")
    private String iban;
    @Column(name = "balance")
    private BigDecimal balance;
}

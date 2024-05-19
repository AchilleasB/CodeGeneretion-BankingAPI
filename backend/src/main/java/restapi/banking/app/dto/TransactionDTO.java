package restapi.banking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.banking.app.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    
    private UUID id;

    private TransactionType type;

    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;

    // the account IBAN from which the transaction is made 
    private String accountFrom;

    // the account IBAN to which the transaction is made
    private String accountTo;

    @Column(name="message")
    private String message;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate timestamp;
}

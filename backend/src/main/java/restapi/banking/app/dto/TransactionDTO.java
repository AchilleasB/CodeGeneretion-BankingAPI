package restapi.banking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.banking.app.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private TransactionType type;

    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;

    // the account IBAN from which the transaction is made 
    private String ibanFrom; //todo: string or Account?????

    // the account IBAN to which the transaction is made
    private String ibanTo;

    @Column(name="message")
    private String message;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID userId;
}

package restapi.banking.app.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.banking.app.model.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMTransactionDTO {
    
    @NotNull(message = "Account ID cannot be null")
    private UUID accountId;

    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;

    @NotNull(message = "transaction type cannot be null")
    private TransactionType transactionType;
}
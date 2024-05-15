package restapi.banking.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.banking.app.model.AccountType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private UUID id;

    private String iban;

    @Min(value = 0, message = "Balance must be greater than or equal to 0")
    private BigDecimal balance;

    @NotNull(message = "Type of account can't be null")
    private AccountType accountType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate openingDate;

    @NotNull(message = "User identifier can't be null")
    private UUID userId;

    @Min(value = 0, message = "Absolute Limit must be greater than or equal to 0")
    private BigDecimal absoluteLimit;

    @Min(value = 0, message = "Daily Limit must be greater than or equal to 0")
    private BigDecimal dailyLimit;

    @Min(value = 0, message = "Transaction Limit must be greater than or equal to 0")
    private BigDecimal transactionLimit;

    private boolean active;
    
}

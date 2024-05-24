package restapi.banking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {
    private BigDecimal amount;
    private String ibanFrom;
    private String ibanTo;
    private String message;

}

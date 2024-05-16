package restapi.banking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private UUID uuid;
    private BigDecimal amount;
    private String ibanFrom;
    private String ibanTo;
    private String message;
    /*---------------Do we need those?----------------
    private String nameFrom
    private String nameTo
    ------------------------------------------------*/
}

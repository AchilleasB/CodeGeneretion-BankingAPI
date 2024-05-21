package restapi.banking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private UUID id;
    private BigDecimal amount;
    private String ibanFrom;
    private String ibanTo;
    private String message;
    private LocalDateTime timestamp;
    /*---------------Do we need those?----------------
    private String nameFrom
    private String nameTo
    ------------------------------------------------*/
}

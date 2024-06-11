package restapi.banking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.banking.app.model.AccountType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IbanDTO {

    private String iban;
    private AccountType accountType;

}

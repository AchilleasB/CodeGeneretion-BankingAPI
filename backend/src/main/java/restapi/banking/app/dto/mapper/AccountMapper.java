package restapi.banking.app.dto.mapper;

import org.modelmapper.ModelMapper;
import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.model.Account;

public class AccountMapper {
    private final ModelMapper mapper = new ModelMapper();

    public AccountDTO toDTO(Account account) {
        return mapper.map(account, AccountDTO.class);
    }


}

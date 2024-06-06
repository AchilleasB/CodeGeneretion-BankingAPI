package restapi.banking.app.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.model.Account;

@Component
public class AccountMapper {
    private final ModelMapper modelMapper;

    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AccountDTO convertAccountToDTO(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }


    public Account convertToAccountEntity(AccountDTO accountDTO) {
        return modelMapper.map(accountDTO, Account.class);
    }
}

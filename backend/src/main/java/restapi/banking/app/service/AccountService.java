package restapi.banking.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import restapi.banking.app.model.Account;
import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.repository.AccountRepository;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final ModelMapper mapper = new ModelMapper();

    public AccountService(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    
    public AccountDTO createAccount(Account account) {
        return null;
    }


    public AccountDTO getAccountByIban(String iban) {
        Account account = accountRepository.findByIban(iban);
        if (account == null) {
            throw new EntityNotFoundException("Account with the following IBAN: " + iban + " not found");
        }
        return convertToDTO(account);
    }
    

    // private functions

    private AccountDTO convertToDTO(Account account) {
        AccountDTO accountDTO = mapper.map(account, AccountDTO.class);
        return accountDTO;
    }

}

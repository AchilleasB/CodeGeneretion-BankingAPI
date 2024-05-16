package restapi.banking.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import restapi.banking.app.dto.mapper.AccountMapper;
import restapi.banking.app.model.Account;
import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.repository.AccountRepository;
import java.util.List;

import java.util.UUID;

@Service
public class AccountService {
    AccountRepository accountRepository ;
    private final AccountMapper accountMapper = new AccountMapper();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public List<AccountDTO> getAccountsByUserId(UUID userId) {
        List<Account> accounts = accountRepository.findAccountByUserId(userId);
        if (accounts.isEmpty()) {
            throw new EntityNotFoundException("No accounts found for user with id: " + userId);
        }
        return accounts.stream().map(accountMapper::toDTO).toList();
    }

}

package restapi.banking.app.service;

import org.springframework.stereotype.Service;
import restapi.banking.app.model.Account;
import restapi.banking.app.repository.AccountRepository;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}

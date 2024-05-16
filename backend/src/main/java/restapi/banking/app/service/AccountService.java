package restapi.banking.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.dto.mapper.AccountMapper;
import restapi.banking.app.dto.mapper.UserMapper;
import restapi.banking.app.model.Account;
import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.model.User;
import restapi.banking.app.repository.AccountRepository;
import restapi.banking.app.repository.UserRepository;

import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::convertAccountToAccountDTO)
                .collect(Collectors.toList());
    }
    public List<AccountDTO> findAccountByUserId(UUID userId) {
        return accountRepository.findAccountsByUserId(userId).stream()
                .map(accountMapper::convertAccountToAccountDTO)
                .collect(Collectors.toList());
    }


}

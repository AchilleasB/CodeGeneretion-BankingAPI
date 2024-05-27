package restapi.banking.app.service;



import org.springframework.stereotype.Service;
import restapi.banking.app.dto.mapper.AccountMapper;
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
    //private final UserRepository userRepository;



    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        //this.userRepository = userRepository;
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

    public AccountDTO createAccount(AccountDTO accountDTO) {

        // Map DTO to entity
        Account account = accountMapper.convertAccountToAccount(accountDTO);

        // Save the account
        Account savedAccount = accountRepository.save(account);

        // Convert saved entity back to DTO
        return accountMapper.convertAccountToAccountDTO(savedAccount);
    }

    public AccountDTO updateAccount(UUID userId, AccountDTO accountDTO) {
        // Fetch the account by its ID
        UUID accountId = accountDTO.getId();
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID is missing");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Ensure the account belongs to the specified user
        if (!account.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this account");
        }

        // Update the fields of the account entity
        account.setAbsoluteLimit(accountDTO.getAbsoluteLimit());
        account.setTransactionLimit(accountDTO.getTransactionLimit());

        // Save the updated account
        Account savedAccount = accountRepository.save(account);

        // Convert the updated entity back to DTO
        return accountMapper.convertAccountToAccountDTO(savedAccount);
    }
    public AccountDTO closeAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setActive(false);  // Mark the account as inactive
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.convertAccountToAccountDTO(updatedAccount);
    }
}













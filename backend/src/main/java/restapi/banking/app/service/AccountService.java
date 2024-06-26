package restapi.banking.app.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import restapi.banking.app.dto.mapper.AccountMapper;
import restapi.banking.app.model.Account;
import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.dto.IbanDTO;
import restapi.banking.app.model.AccountType;
import restapi.banking.app.model.User;
import restapi.banking.app.repository.AccountRepository;
import restapi.banking.app.repository.UserRepository;
import restapi.banking.app.utilities.IBANGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;


    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::convertAccountToDTO)
                .collect(Collectors.toList());
    }
    public List<AccountDTO> findAccountByUserId(UUID userId) {
        return accountRepository.findAccountsByUserId(userId).stream()
                .map(accountMapper::convertAccountToDTO)
                .collect(Collectors.toList());
    }



    public List<AccountDTO> createAccounts(AccountDTO accountDTO) {
        validateAccountDTO(accountDTO);
        LocalDate today = LocalDate.now();
        User user = getUserFromRepository(accountDTO.getUserId());
        Map<String, String> ibans = getIBANsForAccounts();

        AccountDTO savingsAccountDTO = createAndSaveAccount(accountDTO, AccountType.SAVINGS, ibans.get("savings"), today, user);
        AccountDTO checkingAccountDTO = createAndSaveAccount(accountDTO, AccountType.CHECKING, ibans.get("checking"), today, user);

        return List.of(savingsAccountDTO, checkingAccountDTO);
    }

    public List<IbanDTO> findIbansByUserName(String firstName, String lastName) {
        Optional<User> user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        
        if (user.isPresent()) {
            List<Account> accounts = accountRepository.findAccountsByUserId(user.get().getId());

            if (accounts.isEmpty()) {
                throw new EntityNotFoundException(firstName + " " + lastName + " has no approved accounts yet.");
            }

            return accounts.stream()
                    .map(account -> new IbanDTO(account.getIban(), account.getAccountType()))
                    .collect(Collectors.toList());

        } else {
           throw new EntityNotFoundException("Customer not found");
        }
    }

    public AccountDTO deactivateAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setActive(false);
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.convertAccountToDTO(updatedAccount);
    }


    //Private functions
    private User getUserFromRepository(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Map<String, String> getIBANsForAccounts() {
        Map<String, String> ibans = new HashMap<>();
        ibans.put("savings", IBANGenerator.generateIBAN());
        ibans.put("checking", IBANGenerator.generateIBAN());
        return ibans;
    }

    private Account createAccount(AccountDTO accountDTO, AccountType accountType, String iban, LocalDate openingDate, User user) {
        Account account = accountMapper.convertToAccountEntity(accountDTO);
        account.setAccountType(accountType);
        account.setIban(iban);
        account.setOpeningDate(openingDate);
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        return account;
    }


    private void validateAccountDTO(AccountDTO accountDTO) {

        if (accountDTO.getAbsoluteLimit().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Absolute limit cannot be negative");
        }
        if (accountDTO.getTransactionLimit().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction limit cannot be negative");
        }
    }


    private AccountDTO createAndSaveAccount(AccountDTO accountDTO, AccountType accountType, String iban, LocalDate openingDate, User user) {
        Account account = createAccount(accountDTO, accountType, iban, openingDate, user);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.convertAccountToDTO(savedAccount);
    }

    // update account limits by account id
    public AccountDTO updateAccountLimits(UUID accountId, AccountDTO accountDTO) {
        // Validate account DTO
        if (accountDTO.getAbsoluteLimit() == null || accountDTO.getTransactionLimit() == null ||
                accountDTO.getAbsoluteLimit().compareTo(BigDecimal.ZERO) <= 0 ||
                accountDTO.getTransactionLimit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Account limits must be at least 1");
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setAbsoluteLimit(accountDTO.getAbsoluteLimit());
        account.setTransactionLimit(accountDTO.getTransactionLimit());

        Account savedAccount = accountRepository.save(account); // save updated account to repository
        return accountMapper.convertAccountToDTO(savedAccount);
    }

    // Method to toggle the status of an account
    public AccountDTO toggleAccountStatus(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setActive(!account.isActive()); // Toggle the active status
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.convertAccountToDTO(updatedAccount);
    }

    public AccountDTO findAccountByIBAN(String iban) {
        Account account = accountRepository.findByIban(iban);
        if (account == null) {
            throw new EntityNotFoundException("Account not found with IBAN: " + iban);
        }
        return accountMapper.convertAccountToDTO(account);
    }

    //TODO: Check with Dan
    /*public List<AccountDTO> createAccounts(AccountDTO accountDTO) {
        List<Account> accounts = new ArrayList<>();


        String ibanForSavings = IBANGenerator.generateIBAN();
        String ibanForChecking = IBANGenerator.generateIBAN();
        LocalDate today = LocalDate.now();

        User user = userRepository.findById(accountDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create Savings Account
        Account savingsAccount = accountMapper.convertAccountToAccount(accountDTO);
        savingsAccount.setAccountType(AccountType.SAVINGS);
        savingsAccount.setIban(ibanForSavings);
        savingsAccount.setOpeningDate(today);
        savingsAccount.setUser(user);
        accounts.add(savingsAccount);

        // Create Checking Account
        Account checkingAccount = accountMapper.convertAccountToAccount(accountDTO);
        checkingAccount.setAccountType(AccountType.CHECKING);
        checkingAccount.setIban(ibanForChecking);
        checkingAccount.setOpeningDate(today);
        checkingAccount.setUser(user);
        accounts.add(checkingAccount);

        // Save and map both accounts
        List<AccountDTO> createdAccounts = accounts.stream()
                .map(account -> accountRepository.save(account))
                .map(savedAccount -> accountMapper.convertAccountToAccountDTO(savedAccount))
                .collect(Collectors.toList());

        return createdAccounts;
    }
    */

}













package restapi.banking.app.service;



import org.springframework.stereotype.Service;
import restapi.banking.app.dto.mapper.AccountMapper;
import restapi.banking.app.model.Account;
import restapi.banking.app.dto.AccountDTO;

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
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;


    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, UserRepository userRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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



    public List<AccountDTO> createAccounts(AccountDTO accountDTO) {
        LocalDate today = LocalDate.now();
        User user = getUserFromRepository(accountDTO.getUserId());
        Map<String, String> ibans = getIBANsForAccounts();

        AccountDTO savingsAccountDTO = createAndSaveAccount(accountDTO, AccountType.SAVINGS, ibans.get("savings"), today, user);
        AccountDTO checkingAccountDTO = createAndSaveAccount(accountDTO, AccountType.CHECKING, ibans.get("checking"), today, user);

        return List.of(savingsAccountDTO, checkingAccountDTO);
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
        Account account = accountMapper.convertAccountToAccount(accountDTO);
        account.setAccountType(accountType);
        account.setIban(iban);
        account.setOpeningDate(openingDate);
        account.setUser(user);
        return account;
    }

    private AccountDTO createAndSaveAccount(AccountDTO accountDTO, AccountType accountType, String iban, LocalDate openingDate, User user) {
        Account account = createAccount(accountDTO, accountType, iban, openingDate, user);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.convertAccountToAccountDTO(savedAccount);
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













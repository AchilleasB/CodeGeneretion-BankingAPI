package restapi.banking.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import restapi.banking.app.model.Account;
import restapi.banking.app.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

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

    public Account getAccountByIban(String iban) {
        Optional<Account> account = accountRepository.findById(iban);
        return account.orElse(null);
    }
    // update account information
    public Account updateAccount(Account updatedAccount) {
        String iban = updatedAccount.getIban();
        Optional<Account> optionalAccount = accountRepository.findById(iban);
        if (optionalAccount.isPresent()) {
            Account existingAccount = getAccount(updatedAccount, optionalAccount);
            // Save the updated account
            return accountRepository.save(existingAccount);
        } else {
            throw new EntityNotFoundException("Account with IBAN " + iban + " not found");
        }
    }

    private static Account getAccount(Account updatedAccount, Optional<Account> optionalAccount) {
        Account existingAccount = optionalAccount.get();
        // Update fields
        existingAccount.setAccountType(updatedAccount.getAccountType());
        existingAccount.setAccountStatus(updatedAccount.getAccountStatus());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setOpeningDate(updatedAccount.getOpeningDate());
        existingAccount.setDailyLimit(updatedAccount.getDailyLimit());
        existingAccount.setAbsoluteLimit(updatedAccount.getAbsoluteLimit());
        return existingAccount;
    }

}

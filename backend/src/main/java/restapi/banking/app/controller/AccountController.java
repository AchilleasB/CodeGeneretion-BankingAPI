package restapi.banking.app.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.banking.app.model.Account;
import restapi.banking.app.service.AccountService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;

    }
    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    @GetMapping("/{iban}")
    public ResponseEntity<Account> getAccountByIban(@PathVariable String iban) {
        Account account = accountService.getAccountByIban(iban);
        if(account!= null){
            return new ResponseEntity<>(account, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account newAccount = accountService.createAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{iban}")
    public ResponseEntity<Account> updateAccount(@PathVariable String iban, @RequestBody Account updatedAccount) {
        updatedAccount.setIban(iban); // Ensure the IBAN in the path matches the one in the request body
        try {
            Account updated = accountService.updateAccount(updatedAccount);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

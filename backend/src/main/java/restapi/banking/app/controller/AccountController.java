package restapi.banking.app.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.model.Account;
import restapi.banking.app.service.AccountService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.status(200).body(accounts);
    }
    @GetMapping("/{iban}")
    public ResponseEntity<AccountDTO> getAccountByIban(@PathVariable String iban) {
        AccountDTO accountDTO = accountService.getAccountByIban(iban);
        return ResponseEntity.status(200).body(accountDTO);
    }


    // @PostMapping
    // public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    //     Account newAccount = accountService.createAccount(account);
    //     return ResponseEntity.status(201).body(newAccount);
    // }

    // @PutMapping("/{iban}")
    // public ResponseEntity<Account> updateAccount(@PathVariable String iban, @RequestBody Account updatedAccount) {
    //     updatedAccount.setIban(iban); // Ensure the IBAN in the path matches the one in the request body
    //     try {
    //         Account updated = accountService.updateAccount(updatedAccount);
    //         return ResponseEntity<>(updated, HttpStatus.OK);
    //     } catch (EntityNotFoundException e) {
    //         return ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

}

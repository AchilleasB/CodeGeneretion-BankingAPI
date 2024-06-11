package restapi.banking.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.dto.IbanDTO;
import restapi.banking.app.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        try {
            List<AccountDTO> accounts = accountService.getAllAccounts();
            return ResponseEntity.status(HttpStatus.OK).body(accounts);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    public ResponseEntity<List<AccountDTO>> getAccountsByUserId(@PathVariable UUID userId) {
        try {
            List<AccountDTO> accounts = accountService.findAccountByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(accounts);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<AccountDTO>> createAccounts(@RequestBody AccountDTO accountDTO) {
        try {
            List<AccountDTO> createdAccounts = accountService.createAccounts(accountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccounts);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ibans")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity<List<IbanDTO>> getIbansByUserName(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            List<IbanDTO> ibans = accountService.findIbansByUserName(firstName, lastName);
            return ResponseEntity.status(HttpStatus.OK).body(ibans);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{accountId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountDTO> updateAccountLimits(@PathVariable UUID accountId, @RequestBody AccountDTO accountDTO) {
        try {
            AccountDTO updatedAccount = accountService.updateAccountLimits(accountId, accountDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/status/{accountId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountDTO> toggleAccountStatus(@PathVariable UUID accountId) {
        try {
            AccountDTO updatedAccount = accountService.toggleAccountStatus(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/iban/{iban}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountDTO> getAccountByIBAN(@PathVariable String iban) {
        try {
            AccountDTO account = accountService.findAccountByIBAN(iban);
            return ResponseEntity.status(HttpStatus.OK).body(account);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
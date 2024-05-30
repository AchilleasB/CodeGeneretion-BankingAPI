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
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    public List<AccountDTO> getAccountsByUserId(@PathVariable UUID userId) {
        return accountService.findAccountByUserId(userId);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<AccountDTO>> createAccounts(@RequestBody AccountDTO accountDTO) {
        List<AccountDTO> createdAccounts = accountService.createAccounts(accountDTO);
        return new ResponseEntity<>(createdAccounts, HttpStatus.CREATED);
    }

    @GetMapping("/ibans")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity<List<IbanDTO>> getIbansByUserName(@RequestParam String firstName, @RequestParam String lastName) {
        List<IbanDTO> ibans = accountService.findIbansByUserName(firstName, lastName);
        return ResponseEntity.status(HttpStatus.OK).body(ibans);
    }

    @PutMapping("/{accountId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable UUID accountId, @RequestBody AccountDTO accountDTO) {
        try {
            AccountDTO updatedAccount = accountService.updateAccount(accountId, accountDTO);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/deactivate/{accountId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountDTO> deactivateAccount(@PathVariable UUID accountId) {
        try {
            AccountDTO updatedAccount = accountService.deactivateAccount(accountId);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}

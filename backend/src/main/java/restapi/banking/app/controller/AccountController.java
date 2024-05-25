package restapi.banking.app.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import restapi.banking.app.dto.AccountDTO;
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
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

}

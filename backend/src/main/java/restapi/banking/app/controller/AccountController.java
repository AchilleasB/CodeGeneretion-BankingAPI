package restapi.banking.app.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.dto.mapper.AccountMapper;
import restapi.banking.app.dto.mapper.UserMapper;
import restapi.banking.app.model.Account;
import restapi.banking.app.model.User;
import restapi.banking.app.service.AccountService;
import restapi.banking.app.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{userId}")
    public List<AccountDTO> getAccountsByUserId(@PathVariable UUID userId) {
        return accountService.findAccountByUserId(userId);
    }

}

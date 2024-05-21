package restapi.banking.app.controller;

import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.TransactionDTO;
import restapi.banking.app.dto.TransactionRequestDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.service.TransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/createTransaction")
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionDTO createdTransactionDTO = transactionService.createTransaction(transactionRequestDTO);
        return ResponseEntity.status(201).body(createdTransactionDTO);
    }
}

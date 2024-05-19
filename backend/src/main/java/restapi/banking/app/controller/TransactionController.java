package restapi.banking.app.controller;

import restapi.banking.app.dto.ATMTransactionDTO;
import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.TransactionDTO;
import restapi.banking.app.dto.TransactionRequestDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.service.TransactionService;
import restapi.banking.app.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransactionDTO);
    }

    @PostMapping("/atm/withdraw")
    public ResponseEntity<TransactionDTO> atmWithdraw(@Valid @RequestBody ATMTransactionDTO withdrawDTO) {
        TransactionDTO transactionDTO = transactionService.processATMTransaction(withdrawDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO);
    }

    @PostMapping("/atm/deposit")
    public ResponseEntity<TransactionDTO> atmDeposit(@Valid @RequestBody ATMTransactionDTO depositDTO) {
        TransactionDTO transactionDTO = transactionService.processATMTransaction(depositDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO);
    }
}

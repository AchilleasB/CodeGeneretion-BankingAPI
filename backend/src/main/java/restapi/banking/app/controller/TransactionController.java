package restapi.banking.app.controller;

import restapi.banking.app.dto.ATMTransactionDTO;
import restapi.banking.app.dto.TransactionDTO;
import restapi.banking.app.service.TransactionService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("{userId}")
    @PreAuthorize("@securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@PathVariable UUID userId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        List<TransactionDTO> transactions = transactionService.getUserTransactions(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }


    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO transferDTO) {
        TransactionDTO createdTransactionDTO = transactionService.createTransaction(transferDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransactionDTO);
    }

    //todo: /transfer/{iban}

    @PostMapping("/atm/withdraw")
    @PreAuthorize("hasRole('CUSTOMER') and @securityExpressions.isAccountOwner(authentication.principal.id, #withdrawDTO.accountId)")
    public ResponseEntity<TransactionDTO> atmWithdraw(@Valid @RequestBody ATMTransactionDTO withdrawDTO) {
        TransactionDTO transactionDTO = transactionService.processATMTransaction(withdrawDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO);
    }

    @PostMapping("/atm/deposit")
    @PreAuthorize("hasRole('CUSTOMER') and @securityExpressions.isAccountOwner(authentication.principal.id, #depositDTO.accountId)")
    public ResponseEntity<TransactionDTO> atmDeposit(@Valid @RequestBody ATMTransactionDTO depositDTO) {
        TransactionDTO transactionDTO = transactionService.processATMTransaction(depositDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO);
    }
}

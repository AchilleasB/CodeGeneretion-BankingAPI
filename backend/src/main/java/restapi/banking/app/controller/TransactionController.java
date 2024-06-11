package restapi.banking.app.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restapi.banking.app.dto.ATMTransactionDTO;
import restapi.banking.app.dto.TransactionDTO;
import restapi.banking.app.service.TransactionService;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

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
        try {
            List<TransactionDTO> transactions = transactionService.getUserTransactions(userId, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(transactions);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/transfer")
    @PreAuthorize("isAuthenticated() and hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity<?> create(@Valid @RequestBody TransactionDTO transferDTO) {
        try {
            TransactionDTO createdTransactionDTO = transactionService.createTransaction(transferDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransactionDTO);

        } catch (IllegalArgumentException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred while processing the transaction."));
        }
    }

    @PostMapping("/atm/withdraw")
    @PreAuthorize("hasRole('CUSTOMER') and @securityExpressions.isAccountOwner(authentication.principal.id, #withdrawDTO.accountId)")
    public ResponseEntity<?> atmWithdraw(@Valid @RequestBody ATMTransactionDTO withdrawDTO) {
        try {
            TransactionDTO transactionDTO = transactionService.processATMTransaction(withdrawDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred while processing the withdrawal."));
        }
    }

    @PostMapping("/atm/deposit")
    @PreAuthorize("hasRole('CUSTOMER') and @securityExpressions.isAccountOwner(authentication.principal.id, #depositDTO.accountId)")
    public ResponseEntity<?> atmDeposit(@Valid @RequestBody ATMTransactionDTO depositDTO) {
        try {
            TransactionDTO transactionDTO = transactionService.processATMTransaction(depositDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred while processing the deposit."));
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> getTransactionHistory() {
        try {
            List<TransactionDTO> transactionHistory = transactionService.getTransactionHistory();
            return ResponseEntity.status(HttpStatus.OK).body(transactionHistory);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred while retrieving the transaction history."));
        }
    }

}

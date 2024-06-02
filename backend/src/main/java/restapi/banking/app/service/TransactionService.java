package restapi.banking.app.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import restapi.banking.app.dto.ATMTransactionDTO;
import restapi.banking.app.dto.TransactionDTO;

import restapi.banking.app.dto.mapper.TransactionMapper;
import restapi.banking.app.model.*;
import restapi.banking.app.repository.AccountRepository;
import restapi.banking.app.repository.TransactionRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    @Autowired
    private final TransactionMapper transactionMapper;

    public List<TransactionDTO> getUserTransactions(UUID userId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Transaction> transactionsPage = transactionRepository.findByUserId(userId, pageable);

        return transactionsPage.stream()
                .map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO processATMTransaction(ATMTransactionDTO transactionDTO) {
        Account account = accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (transactionDTO.getTransactionType() == TransactionType.WITHDRAW) {
            if (account.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            account.setBalance(account.getBalance().subtract(transactionDTO.getAmount()));
        } else if (transactionDTO.getTransactionType() == TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().add(transactionDTO.getAmount()));
        } else {
            throw new IllegalArgumentException("Unsupported transaction type");
        }

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(transactionDTO.getTransactionType() == TransactionType.WITHDRAW ? account : null);
        transaction.setAccountTo(transactionDTO.getTransactionType() == TransactionType.DEPOSIT ? account : null);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getTransactionType());
        transaction.setUserId(account.getUser().getId());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setMessage("ATM");
        transactionRepository.save(transaction);

        TransactionDTO responseDTO = transactionMapper.convertToDTO(transaction);
        responseDTO.setIbanFrom(
                transactionDTO.getTransactionType() == TransactionType.WITHDRAW ? account.getIban() : null);
        responseDTO
                .setIbanTo(transactionDTO.getTransactionType() == TransactionType.DEPOSIT ? account.getIban() : null);

        return responseDTO;
    }

    public TransactionDTO createTransaction(TransactionDTO requestDTO) {

        isTheAmountCorrect(requestDTO.getAmount());
        areIbansTheSame(requestDTO.getIbanFrom(), requestDTO.getIbanTo());

        // Using authentication to get a user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        //Iban checks
        if(user.getRole().equals(UserRole.Employee))
        {
            IbanValidation(requestDTO.getIbanFrom(), "Sender");
            doesIbanExists(requestDTO.getIbanFrom(), "Sender");
        }
        IbanValidation(requestDTO.getIbanTo(), "Recipient");
        doesIbanExists(requestDTO.getIbanTo(), "Recipient");

        // AccountFrom checks
        Account accountFrom = accountRepository.findByIban(requestDTO.getIbanFrom());
        isEnoughBalance(accountFrom, requestDTO.getAmount());
        checkLimits(accountFrom, requestDTO.getAmount());

        // transferring
        Account accountTo = accountRepository.findByIban(requestDTO.getIbanTo());
        BigDecimal amount = requestDTO.getAmount();
        transferAmounts(accountFrom, accountTo, amount);

        Transaction transaction = new Transaction(); // needs to be done that way to pass correct UUID in the response
        transaction.setAccountTo(accountTo);
        transaction.setAccountFrom(accountFrom);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(requestDTO.getType());
        transaction.setMessage(requestDTO.getMessage());
        transaction.setUserId(user.getId());
        transactionRepository.saveAndFlush(transaction);

        TransactionDTO responceDTO = transactionMapper.convertToDTO(transaction);
        responceDTO.setIbanTo(accountTo.getIban());
        responceDTO.setIbanFrom(accountFrom.getIban());

        testBalance(responceDTO);
        return responceDTO;
    }

    private void IbanValidation(String iban, String whichIban)
    {
        iban = iban.replaceAll("\\s+", "");
        if (iban.length() != 18)
            throw new IllegalArgumentException(whichIban + "'s IBAN must be 18 characters");
        if (!iban.startsWith("NL"))
            throw new IllegalArgumentException(whichIban + "'s IBAN must start with \'NL\'");
        if (!iban.substring(2).matches("\\d{2}[A-Z]{4}\\d{10}"))
            throw new IllegalArgumentException(whichIban + "'s IBAN format is invalid");
        checksum(iban, whichIban);
    }

    // TODO: why query two times for the same IBAN? 
    private void doesIbanExists(String iban, String whichIban)
    {
        Account account = accountRepository.findByIban(iban);
        if(accountRepository.findByIban(iban) == null)
            throw new IllegalArgumentException(whichIban + "'s IBAN does not exist");
    }

    private void isEnoughBalance(Account accountFrom, BigDecimal amountToTransfer) {
        if (accountFrom.getBalance().compareTo(amountToTransfer) < 0)
            throw new IllegalArgumentException("Not enough balance to transfer");
    }

    private void checkLimits(Account accountFrom, BigDecimal amountToTransfer) {
        // check daily limit
        User user = accountFrom.getUser();
        LocalDate localDate = LocalDate.now();
        BigDecimal transferredAmount = transactionRepository.totalTransferred(user.getId(), localDate.atStartOfDay(),
                localDate.atTime(LocalTime.MAX));
        transferredAmount = transferredAmount.add(amountToTransfer); // adding amount to transfer to compare with the limit
        if (transferredAmount.compareTo(BigDecimal.valueOf(user.getDailyLimit())) >= 0)
            throw new IllegalArgumentException("User's daily limit exceeded ");
        // check absolute limit
        BigDecimal accountBalance = accountFrom.getBalance();
        accountBalance.subtract(amountToTransfer);
        if (accountBalance.compareTo(accountFrom.getAbsoluteLimit()) <= 0)
            throw new IllegalArgumentException("Account's absolute limit is exceeded");
    }

    private void checksum(String iban, String whichIban) {
        String accountNumber = iban.substring(8);

        for (int i = 7; i >= 4; i--) {
            char ch = Character.toUpperCase(iban.charAt(i));
            int number = ch - 'A' + 10;
            accountNumber = number + accountNumber;
        }
        accountNumber = accountNumber + "232100"; // 23 - N; 21 - L; 00 required to add
        BigInteger number = new BigInteger(accountNumber);
        number = BigInteger.valueOf(98).subtract(number.mod(BigInteger.valueOf(97)));
        String twoDigits = number.toString();
        // add one zero if less than 10
        if (twoDigits.length() == 1)
            twoDigits = "0" + twoDigits;

        String checkDigits = iban.substring(2, 4);
        if (!twoDigits.equals(checkDigits))
            throw new IllegalArgumentException(whichIban + "'s IBAN is invalid");
    }

    private void transferAmounts(Account accountFrom, Account accountTo, BigDecimal amount) {
        BigDecimal newBalanceFrom = accountFrom.getBalance().subtract(amount);
        accountFrom.setBalance(newBalanceFrom);

        BigDecimal newBalanceTo = accountTo.getBalance().add(amount);
        accountTo.setBalance(newBalanceTo);

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
    }

    private void testBalance(TransactionDTO transactionDTO) {
        Account accountFrom = accountRepository.findByIban(transactionDTO.getIbanFrom());
        Account accountTo = accountRepository.findByIban(transactionDTO.getIbanTo());

        System.out.println("AccountFrom Balance: " + accountFrom.getBalance().toString());
        System.out.println("AccountTo Balance: " + accountTo.getBalance().toString());
        System.out.println();
    }

    private void isTheAmountCorrect(BigDecimal amount) {
        if(amount.compareTo((new BigDecimal("0"))) <= 0)
            throw new IllegalArgumentException("Amount must be higher than €0.00");
    }

    private void areIbansTheSame(String IbanFrom, String IbanTo){
        if(IbanFrom.equals(IbanTo))
            throw new IllegalArgumentException("IBAN of recipient and sender must be different");
    }
}

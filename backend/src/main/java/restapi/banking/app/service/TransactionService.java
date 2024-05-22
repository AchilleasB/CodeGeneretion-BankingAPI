package restapi.banking.app.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import restapi.banking.app.dto.ATMTransactionDTO;
import restapi.banking.app.dto.TransactionDTO;
import restapi.banking.app.dto.TransactionRequestDTO;
import restapi.banking.app.dto.mapper.TransactionMapper;
import restapi.banking.app.model.Account;
import restapi.banking.app.model.Transaction;
import restapi.banking.app.model.TransactionType;
import restapi.banking.app.model.User;
import restapi.banking.app.repository.AccountRepository;
import restapi.banking.app.repository.TransactionRepository;
import restapi.banking.app.repository.UserRepository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional
public class TransactionService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    @Autowired
    private final TransactionMapper transactionMapper;

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
        transaction.setTimestamp(LocalDate.now());
        transactionRepository.save(transaction);

        TransactionDTO responseDTO = transactionMapper.convertToDTO(transaction);
        responseDTO.setAccountFrom(transactionDTO.getTransactionType() == TransactionType.WITHDRAW ? account.getIban() : null);
        responseDTO.setAccountTo(transactionDTO.getTransactionType() == TransactionType.DEPOSIT ? account.getIban() : null);

        return responseDTO;
    }

    public TransactionDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {
        testBalance(transactionRequestDTO);

        IbanValidation(transactionRequestDTO.getIbanTo());
        doesIbanExists(transactionRequestDTO.getIbanTo());
        Account accountFrom = accountRepository.findByIban(transactionRequestDTO.getIbanFrom());
        isEnoughBalance(accountFrom, transactionRequestDTO.getAmount());
        checkLimits(accountFrom, transactionRequestDTO.getAmount)
        Account accountTo = accountRepository.findByIban(transactionRequestDTO.getIbanTo());
        BigDecimal amount = transactionRequestDTO.getAmount();
        transfer(accountFrom, accountTo, amount);
        Transaction transaction = transactionMapper.convertToEntity(transactionRequestDTO);
        //todo:how to set the info to the database if here we have Accounts but in the database that's just id;
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setTimestamp(LocalDate.now()); //timezones?
        transactionRepository.saveAndFlush(transaction);
        TransactionDTO transactionDTO = transactionMapper.convertToDTO(transaction);

        //transactionDTO.setIbanFrom(accountFrom.getIban());
        //transactionDTO.setIbanTo(accountTo.getIban());

        transactionDTO.setAccountFrom(accountFrom.getIban());
        transactionDTO.setAccountTo(accountTo.getIban());
        testBalance(transactionRequestDTO);
        return transactionDTO;
    }

    private void IbanValidation(String iban)
    {
        iban = iban.replaceAll("\\s+", "");
        if (iban.length() != 18)
            throw new IllegalArgumentException("IBAN must be 18 characters");
        if (!iban.startsWith("NL"))
            throw new IllegalArgumentException("IBAN must start with \'NL\'");
        if (!iban.substring(2).matches("\\d{2}[A-Z]{4}\\d{10}"))
            throw new IllegalArgumentException("Invalid IBAN format");
        checksum(iban);

    }
    private void doesIbanExists(String iban)
    {
        if(accountRepository.findByIban(iban) == null)
            throw new NoSuchElementException("Account by Iban does not exist");
    }
    private void isEnoughBalance(Account accountFrom, BigDecimal amountToTransfer)
    {
        if(accountFrom.getBalance().compareTo(amountToTransfer) < 0)
            throw new IllegalArgumentException("Not enough balance to transfer");
    }
    private void checkLimits(Account accountFrom, BigDecimal amountToTransfer)
    {
//        Optional<User> user = userRepository.findByIban(ibanFrom);
//
//        if (user.isEmpty()) {
//            throw new IllegalArgumentException("User with Iban does not exist");
//        }
//        User factualUser = user.get();

        //check daily limit
        User user = accountFrom.getUser();
        BigDecimal transferredAmount = transactionRepository.TotalTransferred(user.getId(), LocalDate.now());
        transferredAmount.add(amountToTransfer); //adding amount to transfer to compare with the limit
        if(transferredAmount.compareTo(BigDecimal.valueOf(user.getDailyLimit())) >= 0)
            throw new IllegalArgumentException("User's daily limit exceeded ");
        //check absolute limit
        BigDecimal accountBalance = accountFrom.getBalance();
        accountBalance.subtract(amountToTransfer);
        if(accountBalance.compareTo(accountFrom.getAbsoluteLimit()) <= 0)
            throw new IllegalArgumentException("Account's absolute limit is exceeded");
    }
    private void checksum(String iban)
    {
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
            throw new IllegalArgumentException("Invalid IBAN");
    }

    private void transfer(Account accountFrom, Account accountTo, BigDecimal amount)
    {
        BigDecimal newBalanceFrom = accountFrom.getBalance().subtract(amount);
        accountFrom.setBalance(newBalanceFrom);

        BigDecimal newBalanceTo = accountTo.getBalance().add(amount);
        accountTo.setBalance(newBalanceTo);

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
    }

    private void testBalance(TransactionRequestDTO transactionRequestDTO)
    {
        Account accountFrom = accountRepository.findByIban(transactionRequestDTO.getIbanFrom());
        Account accountTo = accountRepository.findByIban(transactionRequestDTO.getIbanTo());

        System.out.println("AccountFrom Balance: " + accountFrom.getBalance().toString());
        System.out.println("AccountTo Balance: " + accountTo.getBalance().toString());
        System.out.println();
    }
}

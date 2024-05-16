package restapi.banking.app.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restapi.banking.app.dto.TransactionDTO;
import restapi.banking.app.dto.TransactionRequestDTO;
import restapi.banking.app.model.Account;
import restapi.banking.app.repository.AccountRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    @Autowired
    private final ModelMapper modelMapper;
    public TransactionDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {
        //todo: handle every kind of exception
        //done: validate IBAN
        IbanValidation(transactionRequestDTO.getIbanTo());
        Account account = accountRepository.getReferenceById(transactionRequestDTO.getIbanFrom());
        //TODO: check the limits
        return null;
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
        if(accountRepository.getReferenceById(iban) == null)
            throw new NoSuchElementException("Account by Iban does not exist");
    }
    private void isEnoughBalance(Account accountFrom, BigDecimal amountToTransfer)
    {
        if(accountFrom.getBalance().compareTo(amountToTransfer) < 0)
            throw new IllegalArgumentException("Not enough balance to transfer");
    }
    private static void checksum(String iban)
    {
        String accountNumber = iban.substring(8);
        /* needs to be done only for creating a new account but not while checking
        while(accountNumber.length() < 10)
            accountNumber = "0" + accountNumber;*/
        for(int i = 7; i >= 4; i--)
        {
            char ch = Character.toUpperCase(iban.charAt(i));
            int number = ch - 'A' + 10;
            accountNumber = number + accountNumber;
        }
        accountNumber = accountNumber + "232100"; // 23 - N; 21 - L; 00 required to add
        BigInteger number = new BigInteger(accountNumber);
        number = BigInteger.valueOf(98).subtract(number.mod(BigInteger.valueOf(97)));
        String twoDigits = number.toString();
        //add one zero if less than 10
        if(twoDigits.length() == 1)
            twoDigits = "0" + twoDigits;

        String checkDigits = iban.substring(2,4);
        if(!twoDigits.equals(checkDigits))
            throw new IllegalArgumentException("Invalid IBAN");
    }
}

package restapi.banking.app.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import restapi.banking.app.model.Account;
import restapi.banking.app.model.AccountType;
import restapi.banking.app.model.Transaction;
import restapi.banking.app.model.TransactionType;
import restapi.banking.app.model.User;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.repository.AccountRepository;
import restapi.banking.app.repository.TransactionRepository;
import restapi.banking.app.repository.UserRepository;

@Component
@AllArgsConstructor
@Profile("!test") // This bean will only be created when the profile is not "test"
public class DatabaseInitializer {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct // This method will be called after the bean has been initialized at the start
                   // of the application
    public void initializeData() {

        // Admin user
        User superBank = new User();
        superBank.setFirstName("Super");
        superBank.setLastName("Bank");
        superBank.setDateOfBirth(LocalDate.of(2000, 1, 1));
        superBank.setBsn("123456789");
        superBank.setEmail("superbank@email.com");
        superBank.setPassword(passwordEncoder.encode("password"));
        superBank.setRole(UserRole.Employee);
        superBank.setApproved(true);
        superBank.setDailyLimit(1000000);
        userRepository.saveAndFlush(superBank);

        // Customer 1

        User customer1 = new User();
        customer1.setFirstName("Achil");
        customer1.setLastName("Ballanos");
        customer1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer1.setBsn("31855867");
        customer1.setPhone("0675431290");
        customer1.setEmail("achil@email.com");
        customer1.setPassword(passwordEncoder.encode("achil1234"));
        customer1.setRole(UserRole.Customer);
        customer1.setApproved(true);
        customer1.setDailyLimit(5000);
        userRepository.saveAndFlush(customer1);

        Account account1 = new Account();
        account1.setIban("NL05INHO3456000021");
        account1.setBalance(BigDecimal.valueOf(20560));
        account1.setAccountType(AccountType.CHECKING);
        account1.setOpeningDate(LocalDate.now());
        account1.setUserId(customer1.getId());
        account1.setAbsoluteLimit(BigDecimal.valueOf(100));
        account1.setTransactionLimit(BigDecimal.valueOf(1000));
        account1.setActive(true);
        accountRepository.saveAndFlush(account1);

        Account account2 = new Account();
        account2.setIban("NL31INHO7634150005");
        account2.setBalance(BigDecimal.valueOf(16450));
        account2.setAccountType(AccountType.SAVINGS);
        account2.setOpeningDate(LocalDate.now());
        account2.setUserId(customer1.getId());
        account2.setAbsoluteLimit(BigDecimal.valueOf(100));
        account2.setTransactionLimit(BigDecimal.valueOf(1000));
        account2.setActive(true);
        accountRepository.saveAndFlush(account2);

        initilizeTransactions(customer1, account1);


        // Customer 2

        User customer2 = new User();
        customer2.setFirstName("Tony");
        customer2.setLastName("Stark");
        customer2.setDateOfBirth(LocalDate.of(1970, 1, 1));
        customer2.setBsn("132456378");
        customer2.setPhone("0612563090");
        customer2.setEmail("stark@email.com");
        customer2.setPassword(passwordEncoder.encode("stark1234"));
        customer2.setRole(UserRole.Customer);
        customer2.setApproved(true);
        customer2.setDailyLimit(5000);

        userRepository.saveAndFlush(customer2);

        Account account3 = new Account();
        account3.setIban("NL41INHO3456089001");
        account3.setBalance(BigDecimal.valueOf(32480));
        account3.setAccountType(AccountType.CHECKING);
        account3.setOpeningDate(LocalDate.now());
        account3.setUserId(customer2.getId());
        account3.setAbsoluteLimit(BigDecimal.valueOf(100));
        account3.setTransactionLimit(BigDecimal.valueOf(1000));
        account3.setActive(true);
        accountRepository.saveAndFlush(account3);

//        Account test = accountRepository.findByIban("NL41INHO3456089001");
//        User testUser = test.getUser();

        Account account4 = new Account();
        account4.setIban("NL42INHO7634150001");
        account4.setBalance(BigDecimal.valueOf(128450));
        account4.setAccountType(AccountType.SAVINGS);
        account4.setOpeningDate(LocalDate.now());
        account4.setUserId(customer2.getId());
        account4.setAbsoluteLimit(BigDecimal.valueOf(100));
        account4.setTransactionLimit(BigDecimal.valueOf(1000));
        account4.setActive(true);
        accountRepository.saveAndFlush(account4);

        initilizeTransactions(customer2, account3);


        User customer3 = new User();
        customer3.setFirstName("John");
        customer3.setLastName("Doe");
        customer3.setDateOfBirth(LocalDate.of(1985, 5, 15));
        customer3.setBsn("789456123");
        customer3.setPhone("0612345678");
        customer3.setEmail("john.doe@email.com");
        customer3.setPassword(passwordEncoder.encode("john1234"));
        customer3.setRole(UserRole.Customer);
        customer3.setApproved(false);
        customer3.setDailyLimit(5000);

        userRepository.saveAndFlush(customer3);

    }

    private void initilizeTransactions(User customer, Account account) {
        Transaction transaction1 = new Transaction();
        transaction1.setAccountFrom(account);
        transaction1.setAccountTo(null);
        transaction1.setAmount(BigDecimal.valueOf(1000));
        transaction1.setTimestamp(LocalDateTime.of(2024, 4, 1, 12, 0));
        transaction1.setType(TransactionType.WITHDRAW);
        transaction1.setMessage("ATM");
        transaction1.setUserId(customer.getId());
        transactionRepository.saveAndFlush(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountFrom(null);
        transaction2.setAccountTo(account);
        transaction2.setAmount(BigDecimal.valueOf(2000));
        transaction2.setTimestamp(LocalDateTime.of(2024, 5, 1, 12, 0));
        transaction2.setType(TransactionType.DEPOSIT);
        transaction2.setMessage("ATM");
        transaction2.setUserId(customer.getId());
        transactionRepository.saveAndFlush(transaction2);
    }

}

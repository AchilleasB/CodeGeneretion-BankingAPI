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
     
}

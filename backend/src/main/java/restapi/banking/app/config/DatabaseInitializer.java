package restapi.banking.app.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import restapi.banking.app.model.Account;
import restapi.banking.app.model.User;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.repository.AccountRepository;
import restapi.banking.app.repository.UserRepository;

@Component
@AllArgsConstructor
public class DatabaseInitializer {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct // This method will be called after the bean has been initialized at the start of the application
    public void initializeBank() {
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


        Account testAccount1 = new Account();
        testAccount1.setIban("test123");
        testAccount1.setBalance(new BigDecimal(1000));
        accountRepository.saveAndFlush(testAccount1);

        Account testAccount2 = new Account();
        testAccount2.setIban("test321");
        testAccount2.setBalance(new BigDecimal(2000));
        accountRepository.saveAndFlush(testAccount2);

        //daily limit - how much a user can spend
    }
}

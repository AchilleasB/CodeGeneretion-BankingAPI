package restapi.banking.app.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import restapi.banking.app.model.Account;
import restapi.banking.app.model.AccountType;
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
    public void initializeData() {

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


        User customer1 = new User();
        customer1.setFirstName("Achil");
        customer1.setLastName("Ballanos");
        customer1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer1.setBsn("31855867");
        customer1.setEmail("achil@email.com");
        customer1.setPassword(passwordEncoder.encode("achil1234"));
        customer1.setRole(UserRole.Customer);
        customer1.setApproved(false);
        customer1.setDailyLimit(5000);
        userRepository.saveAndFlush(customer1);



    }


    }



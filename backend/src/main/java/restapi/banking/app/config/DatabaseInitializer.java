package restapi.banking.app.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import restapi.banking.app.model.User;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.repository.UserRepository;

@Component
@AllArgsConstructor
public class DatabaseInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeBank() {
        createSuperBankAccount();
        createTestAccount("John", "Doe", LocalDate.of(1990, 5, 15), "111111111", "john.doe@email.com", "johndoe123", UserRole.Customer, false, 5000);
        createTestAccount("Alice", "Smith", LocalDate.of(1992, 8, 25), "222222222", "alice.smith@email.com", "alicesmith123", UserRole.Customer, false, 10000);
        createTestAccount("Bob", "Johnson", LocalDate.of(1985, 12, 10), "333333333", "bob.johnson@email.com", "bobjohnson123", UserRole.Customer, false, 15000);
    }

    private void createSuperBankAccount() {
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


    private void createTestAccount(String firstName, String lastName, LocalDate dob, String bsn, String email, String password, UserRole role, boolean approved, int dailyLimit) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dob);
        user.setBsn(bsn);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setApproved(approved);
        user.setDailyLimit(dailyLimit);
        userRepository.saveAndFlush(user);
    }


}

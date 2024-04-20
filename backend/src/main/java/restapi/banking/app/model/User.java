package restapi.banking.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor // constructor to initialize all the fields of the class
@NoArgsConstructor // constructor to create instances of the class without passing any arguments
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="bsn")
    private String bsn;

    @Column(name="phone")
    private String phone;

    @Column(name="role")
    private UserRole role;

    @Column(name="approved")
    private boolean approved;

    @Column(name="daily_limit")
    private double dailyLimit;

}

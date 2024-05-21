package restapi.banking.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@AllArgsConstructor // constructor to initialize all the fields of the class
@NoArgsConstructor // constructor to create instances of the class without passing any arguments
@Entity
@Table(name = "users")
public class User implements UserDetails{
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

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private UserRole role;

    @Column(name="approved")
    private boolean approved;

    @Column(name="daily_limit")
    private double dailyLimit;



    // UserDetails interface methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

package restapi.banking.app.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.banking.app.model.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String bsn;
    private String phone;
    private LocalDate dateOfBirth;
    private UserRole role;
    private boolean approved;
    private double dailyLimit;
}

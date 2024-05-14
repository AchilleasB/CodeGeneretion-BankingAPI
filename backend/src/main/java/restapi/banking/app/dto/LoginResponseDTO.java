package restapi.banking.app.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.banking.app.model.UserRole;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private UUID userId;
    private UserRole role;
    private String jwtToken;
}

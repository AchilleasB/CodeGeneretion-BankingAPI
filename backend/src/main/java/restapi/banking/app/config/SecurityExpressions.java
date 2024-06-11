package restapi.banking.app.config;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import restapi.banking.app.model.User;
import restapi.banking.app.repository.UserRepository;

import java.util.UUID;

@Component("securityExpressions")
@AllArgsConstructor
public class SecurityExpressions {

    private final UserRepository userRepository;

    public boolean isAccountOwner(UUID userId, UUID accountId) {
        return userRepository.findById(userId)
                .map(user -> user.getAccounts().stream()
                        .anyMatch(account -> account.getId().equals(accountId)))
                .orElse(false);
    }

    public boolean isSameUserOrEmployee(Authentication authentication, UUID userId) {
        UUID authUserId = ((User) authentication.getPrincipal()).getId();
        boolean isEmployee = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_EMPLOYEE"));
        return authUserId.equals(userId) || isEmployee;
    }

}

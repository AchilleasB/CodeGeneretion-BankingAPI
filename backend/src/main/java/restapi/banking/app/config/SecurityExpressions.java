package restapi.banking.app.config;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import restapi.banking.app.repository.UserRepository;
import restapi.banking.app.model.User;

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
        boolean isEmployee = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        return authUserId.equals(userId) || isEmployee;
    }

    public boolean isLoggedIn(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

}

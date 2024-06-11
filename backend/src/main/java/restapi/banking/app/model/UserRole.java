package restapi.banking.app.model;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@ToString
public enum UserRole implements GrantedAuthority {
    Customer,
    Employee;

    @Override
    public String getAuthority() {
        return name();
    }

}



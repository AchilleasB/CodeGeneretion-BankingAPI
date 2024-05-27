package restapi.banking.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restapi.banking.app.model.User;
import restapi.banking.app.model.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID userId);
    void deleteById(UUID userId);
    List<User> findByApprovedAndRole(boolean isApproved, UserRole role);


    List<User> findByApproved(boolean isApproved);
    //Optional<User> findByIban(String iban);
    
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

}

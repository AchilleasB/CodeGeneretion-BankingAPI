package restapi.banking.app.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.dto.mapper.UserMapper;
import restapi.banking.app.model.User;
import restapi.banking.app.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
    }
    public User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
    }

    private void isUserAdult(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.plusYears(18).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("User should be at least 18 years old");
        }
    }

    public List<User> findUnapprovedUsers() {
        List<User> unapprovedUsers = userRepository.findByApproved(false);
        if (unapprovedUsers.isEmpty()) {
            throw new IllegalArgumentException("No unapproved users found");
        }
        return unapprovedUsers;
    }


}

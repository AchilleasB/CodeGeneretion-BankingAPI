package restapi.banking.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.dto.mapper.UserMapper;
import restapi.banking.app.model.User;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${default.dailyLimit:5000}")
    private Double dailyLimit;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::convertUserToUserDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
    }

    private void isUserAdult(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.plusYears(18).isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User should be at least 18 years old");
        }
    }

    public List<UserDTO> findUnapprovedUsers() {
        List<UserDTO> unapprovedUserDTOs = userRepository.findByApprovedAndRole(false, UserRole.Customer).stream()
                .map(userMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
        if (unapprovedUserDTOs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No unapproved users found");
        }
        return unapprovedUserDTOs;
    }

    public List<UserDTO> findApprovedUsers() {
        return userRepository.findByApprovedAndRole(true, UserRole.Customer).stream()
                //.filter(user -> user.getAccounts() == null || user.getAccounts().isEmpty())
                .map(userMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO approveUser(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setApproved(true);
            user.setDailyLimit(dailyLimit);
            userRepository.save(user);
            return userMapper.convertUserToUserDTO(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }
    }

    public void declineUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        userRepository.delete(user);
    }

    public UserDTO updateDailyLimit(UUID userId, double newDailyLimit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        user.setDailyLimit(newDailyLimit);
        userRepository.save(user);

        return userMapper.convertUserToUserDTO(user);
    }

}

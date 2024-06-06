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

import java.lang.Double;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountService accountRepository;
    @Value("${default.dailyLimit:5000}")

    private Double dailyLimit;

    public UserService(UserRepository userRepository, UserMapper userMapper, AccountService accountRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.accountRepository = accountRepository;
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

    public List<UserDTO> findUnapprovedUsers() {
        List<UserDTO> unapprovedUserDTOs = userRepository.findByApprovedAndRole(false, UserRole.Customer).stream()
                .map(userMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
        if (unapprovedUserDTOs.isEmpty()) {
            throw new RuntimeException("User not found with id: " + UUID.randomUUID());
        }
        return unapprovedUserDTOs;
    }
    public List<UserDTO> findApprovedUsersWithoutAccount() {
        return userRepository.findByApprovedAndRole(true, UserRole.Customer).stream()
                //.filter(user -> user.getAccounts() == null || user.getAccounts().isEmpty())
                .filter(user -> accountRepository.findAccountByUserId(user.getId()).isEmpty())
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
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    public void declineUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
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

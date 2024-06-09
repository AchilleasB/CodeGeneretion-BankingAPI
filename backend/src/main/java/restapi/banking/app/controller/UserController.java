package restapi.banking.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/unapproved")
    public ResponseEntity<List<UserDTO>> listUnapprovedUsers() {
        try {
            List<UserDTO> userDTOs = userService.findUnapprovedUsers();
            return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/approved")
    public ResponseEntity<List<UserDTO>> listApprovedUsers() {
        try {
            List<UserDTO> userDTOs = userService.findApprovedUsersWithoutAccount();
            return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/approve/{userId}")
    public ResponseEntity<UserDTO> approveUser(@PathVariable UUID userId) {
        try {
            UserDTO approvedUser = userService.approveUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(approvedUser);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/decline/{id}")
    public ResponseEntity<String> declineUser(@PathVariable UUID id) {
        try {
            userService.declineUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("User declined successfully.");
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE') or @securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID userId) {
        try {
            UserDTO user = userService.findUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateDailyLimit(@PathVariable UUID userId, @RequestBody UserDTO userDTO) {

        try {
            if (userDTO.getDailyLimit() <= 0) {
                return ResponseEntity.badRequest().body(null);
            }
            UserDTO updatedUser = userService.updateDailyLimit(userId, userDTO.getDailyLimit());
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

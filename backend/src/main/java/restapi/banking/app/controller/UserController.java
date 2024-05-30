package restapi.banking.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restapi.banking.app.dto.AccountDTO;
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

    @PreAuthorize("hasRole('EMPLOYEE') or @securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    @GetMapping("/unapproved")
    public ResponseEntity<List<UserDTO>> listUnapprovedUsers() {
        List<UserDTO> userDTOs = userService.findUnapprovedUsers();
        return ResponseEntity.ok(userDTOs);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or @securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    @GetMapping("/approved")
    public ResponseEntity<List<UserDTO>> listApprovedUsers() {
        List<UserDTO> userDTOs = userService.findApprovedUsers();
        return ResponseEntity.ok(userDTOs);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or @securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    @PostMapping("/approve/{userId}")
    public ResponseEntity<UserDTO> approveUser(@PathVariable UUID userId) {
        UserDTO approvedUser=userService.approveUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(approvedUser);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or @securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    @DeleteMapping("/decline/{id}")

    public ResponseEntity<String> declineUser(@PathVariable UUID id) {
        userService.declineUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User declined successfully.");
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('EMPLOYEE') or @securityExpressions.isSameUserOrEmployee(authentication, #userId)")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID userId) {
        UserDTO user = userService.findUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateDailyLimit(
            @PathVariable UUID userId,
            @RequestBody UserDTO userDTO) {
        if (userDTO.getDailyLimit() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        UserDTO updatedUser = userService.updateDailyLimit(userId, userDTO.getDailyLimit());
        return ResponseEntity.ok(updatedUser);
    }




}

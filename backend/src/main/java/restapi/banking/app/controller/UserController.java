package restapi.banking.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restapi.banking.app.dto.UserDTO;
import org.springframework.web.bind.annotation.*;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.dto.mapper.UserMapper;
import restapi.banking.app.model.User;
import restapi.banking.app.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('Employee')")
    @GetMapping("/unapproved")
    public ResponseEntity<List<User>> listUnapprovedUsers() {
        List<User> users = userService.findUnapprovedUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable UUID userId) {
        User user = userService.findUserById(userId);
        return userMapper.convertUserToUserDTO(user);

    }
}

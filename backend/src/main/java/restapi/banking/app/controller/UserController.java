package restapi.banking.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

import java.util.Collections;
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
    public ResponseEntity<List<UserDTO>> listUnapprovedUsers() {
        List<User> users = userService.findUnapprovedUsers();
        List<UserDTO> userDTOs = userMapper.convertUserListToUserDTOList(users);
        return ResponseEntity.ok(userDTOs);
    }

    @PreAuthorize("hasRole('Employee')")
    @GetMapping("/approved")
    public ResponseEntity<List<UserDTO>> listApprovedUsers() {
        List<User> users = userService.findApprovedUsers();
        List<UserDTO> userDTOs = userMapper.convertUserListToUserDTOList(users);
        return ResponseEntity.ok(userDTOs);
    }

    @PreAuthorize("hasRole('Employee')")
    @PostMapping("/approve/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable UUID userId) {
        userService.approveUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("User approved successfully.");
    }
    @PreAuthorize("hasRole('Employee')")
    @DeleteMapping("/decline/{id}")
    public ResponseEntity<String> declineUser(@PathVariable UUID id) {
        userService.declineUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User declined successfully.");
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

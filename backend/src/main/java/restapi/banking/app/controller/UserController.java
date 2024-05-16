package restapi.banking.app.controller;

import lombok.AllArgsConstructor;
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

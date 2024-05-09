package restapi.banking.app.controller;

import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.LoginResponseDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.service.UserService;

import javax.naming.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    
    private final UserService userService;  

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
        UserDTO registeringUserDTO = userService.register(registrationDTO);
        return ResponseEntity.status(201).body(registeringUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO) throws AuthenticationException {
        LoginResponseDTO loggingInDTO = userService.login(loginDTO);
        return ResponseEntity.status(200).body(loggingInDTO);
    }
}

package restapi.banking.app.controller;

import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.LoginResponseDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.service.AuthService;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()") // users can't register if they're already logged in
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
        UserDTO registeringUserDTO = authService.register(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeringUserDTO);

    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()") // users can't login if they're already logged in
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO){
        LoginResponseDTO loggingInDTO = authService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(loggingInDTO);
    }

    // security configuration handles logout
    // should I make a LogoutDTO and return it?
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}

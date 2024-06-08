package restapi.banking.app.controller;

import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.LoginResponseDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.service.AuthService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
        try {
            UserDTO registeringUserDTO = authService.register(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeringUserDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the registration.");
        }

    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()") // users can't login if they're already logged in
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        try {
            LoginResponseDTO loggingInDTO = authService.login(loginDTO);
            return ResponseEntity.status(HttpStatus.OK).body(loggingInDTO);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the login.");
        }

    }

    // security configuration handles logout
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

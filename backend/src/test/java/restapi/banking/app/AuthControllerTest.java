package restapi.banking.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.LoginResponseDTO;
import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.service.AuthService;
import restapi.banking.app.exception.UserNotApprovedException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    // registration tests
    @Test
    public void testRegisterSuccess() throws Exception {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .bsn("123456789")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .bsn("123456789")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .role(UserRole.Customer)
                .approved(false)
                .dailyLimit(0.0)
                .build();

        given(authService.register(any(RegistrationDTO.class))).willReturn(userDTO);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.bsn").value("123456789"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.dateOfBirth").value("01-01-1990"))
                .andExpect(jsonPath("$.role").value("Customer"))
                .andExpect(jsonPath("$.approved").value(false))
                .andExpect(jsonPath("$.dailyLimit").value(0.0));
    }

    @Test
    public void testRegisterInvalidEmail() throws Exception {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email")
                .password("password")
                .bsn("123456789")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        given(authService.register(any(RegistrationDTO.class))).willThrow(new IllegalArgumentException("Invalid email format"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email format"));
    }

    @Test
    public void testRegisterUnderageUser() throws Exception {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .bsn("123456789")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.now().minusYears(10)) // User is underage
                .build();

        given(authService.register(any(RegistrationDTO.class))).willThrow(new IllegalArgumentException("User must be an adult"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User must be an adult"));
    }

    @Test
    public void testRegisterEmailAlreadyExists() throws Exception {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .bsn("123456789")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        given(authService.register(any(RegistrationDTO.class))).willThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    // login tests
    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .bsn("123456789")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .role(UserRole.Customer)
                .approved(true)
                .dailyLimit(0.0)
                .build();

        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .jwtToken("fake-jwt-token")
                .user(userDTO)
                .build();

        given(authService.login(any(LoginRequestDTO.class))).willReturn(loginResponseDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").value("fake-jwt-token"))
                .andExpect(jsonPath("$.user.id").exists())
                .andExpect(jsonPath("$.user.firstName").value("John"))
                .andExpect(jsonPath("$.user.lastName").value("Doe"))
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.user.bsn").value("123456789"))
                .andExpect(jsonPath("$.user.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.user.dateOfBirth").value("01-01-1990"))
                .andExpect(jsonPath("$.user.role").value("Customer"))
                .andExpect(jsonPath("$.user.approved").value(true))
                .andExpect(jsonPath("$.user.dailyLimit").value(0.0));
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email("john.doe@example.com")
                .password("wrongpassword")
                .build();

        given(authService.login(any(LoginRequestDTO.class))).willThrow(new BadCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    public void testLoginUserNotApproved() throws Exception {
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();

        given(authService.login(any(LoginRequestDTO.class))).willThrow(new UserNotApprovedException("John Doe is waiting for approval."));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("John Doe is waiting for approval."));
    }
    
}

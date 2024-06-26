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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.LoginResponseDTO;
import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.model.User;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.repository.UserRepository;
import restapi.banking.app.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AuthService authService;

        @MockBean
        private UserRepository userRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private PasswordEncoder passwordEncoder;

        // Register tests
        @Test
        public void testRegisterSuccess() throws Exception {
                RegistrationDTO registrationDTO = RegistrationDTO.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .password("password")
                                .bsn("123456789")
                                .phone("1234567890")
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .build();

                UserDTO userDTO = UserDTO.builder()
                                .id(UUID.randomUUID())
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .bsn("123456789")
                                .phone("1234567890")
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
                                .andExpect(jsonPath("$.firstName").value("John"))
                                .andExpect(jsonPath("$.lastName").value("Doe"))
                                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                                .andExpect(jsonPath("$.bsn").value("123456789"))
                                .andExpect(jsonPath("$.phone").value("1234567890"))
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
                                .email("invalid-email-format")
                                .password("password")
                                .bsn("123456789")
                                .phone("1234567890")
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .build();

                given(authService.register(any(RegistrationDTO.class)))
                                .willThrow(new IllegalArgumentException("Invalid email format"));

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Invalid email format"));
        }

        @Test
        public void testRegisterEmailAlreadyExists() throws Exception {
                RegistrationDTO registrationDTO = RegistrationDTO.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .password("password")
                                .bsn("123456789")
                                .phone("1234567890")
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .build();

                given(authService.register(any(RegistrationDTO.class)))
                                .willThrow(new IllegalArgumentException("Email already exists"));

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Email already exists"));
        }

        // Login tests
        @Test
        public void testLoginSuccess() throws Exception {
                LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                                .email("john.doe@example.com")
                                .password("password")
                                .build();

                User user = User.builder()
                                .id(UUID.randomUUID())
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .password(passwordEncoder.encode("password"))
                                .bsn("123456789")
                                .phone("1234567890")
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .role(UserRole.Customer)
                                .approved(true)
                                .dailyLimit(0.0)
                                .build();

                UserDTO userDTO = UserDTO.builder()
                                .id(user.getId())
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .bsn("123456789")
                                .phone("1234567890")
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .role(UserRole.Customer)
                                .approved(true)
                                .dailyLimit(0.0)
                                .build();

                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                                .jwtToken("fake-jwt-token")
                                .user(userDTO)
                                .build();

                given(userRepository.findByEmail("john.doe@example.com")).willReturn(Optional.of(user));
                given(passwordEncoder.matches("password", user.getPassword())).willReturn(true);
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
                                .andExpect(jsonPath("$.user.phone").value("1234567890"))
                                .andExpect(jsonPath("$.user.dateOfBirth").value("01-01-1990"))
                                .andExpect(jsonPath("$.user.role").value("Customer"))
                                .andExpect(jsonPath("$.user.approved").value(true))
                                .andExpect(jsonPath("$.user.dailyLimit").value(0.0));
        }

        @Test
        public void testLoginInvalidEmail() throws Exception {
                LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                                .email("non.existent@example.com")
                                .password("password")
                                .build();

                // Mock the service method to throw an exception for invalid email
                given(authService.login(any(LoginRequestDTO.class)))
                                .willThrow(new BadCredentialsException("Invalid email"));

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.message").value("Invalid email"));
        }

        @Test
        public void testLoginInvalidPassword() throws Exception {
                LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                                .email("john.doe@example.com")
                                .password("wrongpassword")
                                .build();

                User user = User.builder()
                                .id(UUID.randomUUID())
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@example.com")
                                .password("encodedpassword")
                                .bsn("123456789")
                                .phone("1234567890")
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .role(UserRole.Customer)
                                .approved(true)
                                .dailyLimit(0.0)
                                .build();

                // Mock the repository to return a user
                given(userRepository.findByEmail("john.doe@example.com")).willReturn(Optional.of(user));
                // Mock the password encoder to return false for incorrect password
                given(passwordEncoder.matches("wrongpassword", "encodedpassword")).willReturn(false);

                // Mock the service method to throw an exception for invalid password
                given(authService.login(any(LoginRequestDTO.class)))
                                .willThrow(new BadCredentialsException("Invalid password"));

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.message").value("Invalid password"));
        }
}

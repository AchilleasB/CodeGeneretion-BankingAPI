package restapi.banking.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.LoginResponseDTO;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import org.springframework.security.core.AuthenticationException;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("achil@example.com", "achil1234");
        LoginResponseDTO loginResponse = new LoginResponseDTO(
                UUID.randomUUID(),
                "Achil",
                UserRole.Customer,
                "random-jwt-token-12345");

        given(authService.login(any(LoginRequestDTO.class))).willReturn(loginResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.jwtToken").value("random-jwt-token-12345"));
    }

    // @Test
    // public void testLoginFailure() throws Exception {
    //     LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "wrongpassword");

    //     given(authService.login(any(LoginRequestDTO.class)))
    //         .willThrow(new AuthenticationException("Invalid credentials"));

    //     mockMvc.perform(post("/auth/login")
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(new ObjectMapper().writeValueAsString(loginRequest)))
    //         .andExpect(status().isUnauthorized());
    //             .andExpect(status().isUnauthorized());
    // }
}

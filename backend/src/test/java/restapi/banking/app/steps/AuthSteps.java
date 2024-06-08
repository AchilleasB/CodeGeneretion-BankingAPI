package restapi.banking.app.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import restapi.banking.app.AppApplication;
import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.RegistrationDTO;

@CucumberContextConfiguration
@SpringBootTest(classes = AppApplication.class)
@AutoConfigureMockMvc
public class AuthSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RegistrationDTO registrationDTO;
    private LoginRequestDTO loginRequestDTO;
    private int responseStatus;
    private String responseMessage;

    @Given("a user with an invalid email {string}")
    public void a_user_with_an_invalid_email(String email) {
        registrationDTO = RegistrationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .password("password")
                .bsn("123456789")
                .phone("1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    @When("the user attempts to register")
    public void the_user_attempts_to_register() throws Exception {
        var result = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andReturn();
        responseStatus = result.getResponse().getStatus();
        responseMessage = result.getResponse().getContentAsString();
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int status) {
        assertThat(responseStatus).isEqualTo(status);
    }

    @Then("the response message should be {string}")
    public void the_response_message_should_be(String message) {
        assertThat(responseMessage).contains(message);
    }

    @Given("a user with email {string} and password {string}")
    public void a_user_with_email_and_password(String email, String password) {
        loginRequestDTO = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();
    }

    @When("the user attempts to login")
    public void the_user_attempts_to_login() throws Exception {
        var result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andReturn();
        responseStatus = result.getResponse().getStatus();
        responseMessage = result.getResponse().getContentAsString();
    }

}

package restapi.banking.app.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import restapi.banking.app.AppApplication;
import restapi.banking.app.dto.LoginRequestDTO;


@SpringBootTest(classes = AppApplication.class)
@AutoConfigureMockMvc
public class AccountSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private int responseStatus;
    private String responseContent;

    @Given("I am an authenticated EMPLOYEE")
    public void i_am_an_authenticated_employee() throws Exception {
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email("superbank@email.com")
                .password("password")
                .build();

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andReturn();

        responseStatus = result.getResponse().getStatus();
        responseContent = result.getResponse().getContentAsString();

        System.out.println("Login response status: " + responseStatus);
        System.out.println("Login response content: " + responseContent);

        if (responseStatus == 200) {
            Map<String, Object> responseMap = objectMapper.readValue(responseContent, Map.class);
            token = (String) responseMap.get("jwtToken");
            System.out.println("Extracted token: " + token);

            if (token == null || token.split("\\.").length != 3) {
                throw new RuntimeException("Invalid JWT token received");
            }
        } else {
            throw new RuntimeException("Failed to authenticate, response status: " + responseStatus);
        }
    }

    @When("I request to get all accounts")
    public void i_request_to_get_all_accounts() throws Exception {
        MvcResult result = mockMvc.perform(get("/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        responseStatus = result.getResponse().getStatus();
        responseContent = result.getResponse().getContentAsString();

        System.out.println("Get all accounts response status: " + responseStatus);
        System.out.println("Get all accounts response content: " + responseContent);
    }

    @Then("I should receive a list of all accounts")
    public void i_should_receive_a_list_of_all_accounts() {
        assertThat(responseContent).isNotBlank();
        try {
            List<?> accounts = objectMapper.readValue(responseContent, List.class);
            assertThat(accounts).isNotEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            assertThat(false).isTrue(); // Force test failure
        }
    }

    @Then("the response status should be {int} OK")
    public void the_response_status_should_be_ok(int status) {
        assertThat(responseStatus).isEqualTo(status);
    }
}
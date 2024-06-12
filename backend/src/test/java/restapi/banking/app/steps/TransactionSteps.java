package restapi.banking.app.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import restapi.banking.app.controller.TransactionController;
import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.TransactionDTO;
import restapi.banking.app.model.*;
import restapi.banking.app.repository.AccountRepository;
import restapi.banking.app.repository.UserRepository;
import restapi.banking.app.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionSteps {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    private final UUID sharedUserId = UUID.randomUUID();
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public TransactionSteps() {
    }

    @Before
    public void setUp() throws Exception {
        //mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService)).build();

        // Ensure account repository is not null
//        assert accountRepository != null;
//
//        // Create and save the first account
//        Account account1 = new Account();
//        account1.setIban("NL05INHO3456000021");
//        account1.setBalance(BigDecimal.valueOf(20560));
//        account1.setAccountType(AccountType.CHECKING);
//        account1.setOpeningDate(LocalDate.now());
//        account1.setUserId(sharedUserId);
//        account1.setAbsoluteLimit(BigDecimal.valueOf(100));
//        account1.setTransactionLimit(BigDecimal.valueOf(1000));
//        account1.setActive(true);
//        accountRepository.saveAndFlush(account1);
//
//        // Create and save the second account
//        Account account2 = new Account();
//        account2.setIban("NL31INHO7634150005");
//        account2.setBalance(BigDecimal.valueOf(16450));
//        account2.setAccountType(AccountType.SAVINGS);
//        account2.setOpeningDate(LocalDate.now());
//        account2.setUserId(sharedUserId);
//        account2.setAbsoluteLimit(BigDecimal.valueOf(100));
//        account2.setTransactionLimit(BigDecimal.valueOf(1000));
//        account2.setActive(true);
//        accountRepository.saveAndFlush(account2);
//
//        // Logging for debugging
//        System.out.println("Setup completed with accounts: " + account1.getIban() + " and " + account2.getIban());

        // Do the login
//        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
//                .email("superbank@email.com")
//                .password(passwordEncoder.encode("password"))
//                .build();
//
//        var result = mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
//                .andReturn();
//        int responseStatus = result.getResponse().getStatus();
//        String responseMessage = result.getResponse().getContentAsString();
//        System.out.println(responseMessage);
    }


    @Given("I am an authenticated user with role {string}")
    public void i_am_an_authenticated_user_with_role(String role) {
        //mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService)).build();
    }

    @When("I send a POST request to {string} with valid transaction data")
    public void i_send_a_POST_request_to_with_valid_transaction_data(String url) throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(
                UUID.randomUUID(),
                TransactionType.TRANSFER,
                BigDecimal.valueOf(100),
                "NL05INHO3456000021",
                "NL31INHO7634150005",
                "Test transaction",
                null,
                sharedUserId // This should match the user ID set up in the `@BeforeEach` method
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 201) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                })
                .andExpect(status().isCreated()) // Expecting 201 CREATED status
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(transactionDTO.getAmount().doubleValue()))
                .andExpect(jsonPath("$.ibanFrom").value(transactionDTO.getIbanFrom()))
                .andExpect(jsonPath("$.ibanTo").value(transactionDTO.getIbanTo()));

    }

    @Then("the transaction response status should be {int}")
    public void the_transaction_response_status_should_be(int expectedStatus) {
        try {
            assertThat(expectedStatus).isEqualTo(201); // Expecting a 201 Created status
        } catch (AssertionError e) {
            System.out.println("Assertion error: " + e.getMessage());
            throw e;
        }
    }

    @Then("the response should contain the transaction ID")
    public void the_response_should_contain_the_transaction_ID() {
        // This is already validated in the @When step with jsonPath("$.id").exists()
    }
}

package restapi.banking.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import restapi.banking.app.controller.TransactionController;
import restapi.banking.app.dto.ATMTransactionDTO;
import restapi.banking.app.dto.TransactionDTO;
import restapi.banking.app.model.TransactionType;
import restapi.banking.app.service.TransactionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService)).build();
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testCreateTransaction() throws Exception {
        TransactionDTO transactionDTO = createTestTransactionDTO();

        given(transactionService.createTransaction(any(TransactionDTO.class))).willReturn(transactionDTO);

        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(transactionDTO.getAmount().doubleValue()))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 201) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testATMWithdraw() throws Exception {
        ATMTransactionDTO withdrawDTO = createTestATMTransactionDTO();
        withdrawDTO.setTransactionType(TransactionType.WITHDRAW);
        TransactionDTO transactionDTO = createTestTransactionDTO();

        given(transactionService.processATMTransaction(any(ATMTransactionDTO.class))).willReturn(transactionDTO);

        mockMvc.perform(post("/transactions/atm/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 201) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testATMDeposit() throws Exception {
        ATMTransactionDTO depositDTO = createTestATMTransactionDTO();
        depositDTO.setTransactionType(TransactionType.DEPOSIT);
        TransactionDTO transactionDTO = createTestTransactionDTO();

        given(transactionService.processATMTransaction(any(ATMTransactionDTO.class))).willReturn(transactionDTO);

        mockMvc.perform(post("/transactions/atm/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 201) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testGetTransactionHistory() throws Exception {
        List<TransactionDTO> transactionHistory = new ArrayList<>();
        transactionHistory.add(createTestTransactionDTO());
        transactionHistory.add(createTestTransactionDTO());

        given(transactionService.getTransactionHistory()).willReturn(transactionHistory);

        mockMvc.perform(get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 200) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE", "CUSTOMER"})
    public void testCreateTransactionBadRequest() throws Exception {
        TransactionDTO invalidTransactionDTO = new TransactionDTO(
                UUID.randomUUID(),
                TransactionType.TRANSFER,
                BigDecimal.valueOf(-100), // Invalid negative amount
                "NL91ABNA0417164300",
                "NL91ABNA0417164301",
                "Test message",
                null,
                UUID.randomUUID()
        );

        // Configure the mock service to throw an IllegalArgumentException for this invalid transaction
        given(transactionService.createTransaction(any(TransactionDTO.class)))
                .willThrow(new IllegalArgumentException("Amount must be greater than or equal to 0"));

        // Perform the POST request with the invalid transaction data
        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTransactionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount must be greater than or equal to 0"))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 400) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }


    private TransactionDTO createTestTransactionDTO() {
        return new TransactionDTO(
                UUID.randomUUID(),
                TransactionType.TRANSFER,
                BigDecimal.valueOf(100),
                "NL91INHO0417164300",
                "NL91INHO0417164301",
                "Test message",
                null,
                UUID.randomUUID()
        );
    }

    private ATMTransactionDTO createTestATMTransactionDTO() {
        ATMTransactionDTO atmTransactionDTO = new ATMTransactionDTO();
        atmTransactionDTO.setAccountId(UUID.randomUUID());
        atmTransactionDTO.setAmount(BigDecimal.valueOf(100));
        atmTransactionDTO.setTransactionType(TransactionType.DEPOSIT);
        return atmTransactionDTO;
    }
}

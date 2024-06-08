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
import restapi.banking.app.controller.AccountController;
import restapi.banking.app.dto.AccountDTO;
import restapi.banking.app.dto.IbanDTO;
import restapi.banking.app.model.AccountType;
import restapi.banking.app.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AccountController(accountService)).build();
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testGetAllAccounts() throws Exception {
        List<AccountDTO> accounts = new ArrayList<>();
        accounts.add(createTestAccountDTO());
        accounts.add(createTestAccountDTO());

        given(accountService.getAllAccounts()).willReturn(accounts);

        mockMvc.perform(get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testGetAccountsByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        List<AccountDTO> accounts = new ArrayList<>();
        accounts.add(createTestAccountDTO());
        accounts.add(createTestAccountDTO());

        given(accountService.findAccountByUserId(userId)).willReturn(accounts);

        mockMvc.perform(get("/accounts/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testCreateAccounts() throws Exception {
        AccountDTO accountDTO = createTestAccountDTO();
        List<AccountDTO> createdAccounts = new ArrayList<>();
        createdAccounts.add(accountDTO);

        given(accountService.createAccounts(any(AccountDTO.class))).willReturn(createdAccounts);

        mockMvc.perform(post("/accounts/{userId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].iban").value("DE89370400440532013000"))
                .andExpect(jsonPath("$[0].balance").value(1000))
                .andExpect(jsonPath("$[0].accountType").value("SAVINGS"))
                .andExpect(jsonPath("$[0].openingDate").value("01-01-2023"))
                .andExpect(jsonPath("$[0].userId").exists())
                .andExpect(jsonPath("$[0].absoluteLimit").value(100))
                .andExpect(jsonPath("$[0].transactionLimit").value(500))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE", "CUSTOMER"})
    public void testGetIbansByUserName() throws Exception {
        List<IbanDTO> ibans = new ArrayList<>();
        IbanDTO iban1 = new IbanDTO("DE89370400440532013000", AccountType.SAVINGS);
        IbanDTO iban2 = new IbanDTO("DE89370400440532013001", AccountType.CHECKING);
        ibans.add(iban1);
        ibans.add(iban2);

        given(accountService.findIbansByUserName("John", "Doe")).willReturn(ibans);

        mockMvc.perform(get("/accounts/ibans")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].iban").value("DE89370400440532013000"))
                .andExpect(jsonPath("$[1].iban").value("DE89370400440532013001"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testUpdateAccount() throws Exception {
        UUID accountId = UUID.randomUUID();
        AccountDTO accountDTO = createTestAccountDTO();

        given(accountService.updateAccount(eq(accountId), any(AccountDTO.class))).willReturn(accountDTO);

        mockMvc.perform(put("/accounts/{accountId}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.iban").value("DE89370400440532013000"))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.openingDate").value("01-01-2023"))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.absoluteLimit").value(100))
                .andExpect(jsonPath("$.transactionLimit").value(500))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testToggleAccountStatus() throws Exception {
        UUID accountId = UUID.randomUUID();
        AccountDTO accountDTO = createTestAccountDTO();

        given(accountService.toggleAccountStatus(accountId)).willReturn(accountDTO);

        mockMvc.perform(put("/accounts/status/{accountId}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testGetAccountByIBAN() throws Exception {
        String iban = "DE89370400440532013000";
        AccountDTO accountDTO = createTestAccountDTO();

        given(accountService.findAccountByIBAN(iban)).willReturn(accountDTO);

        mockMvc.perform(get("/accounts/iban/{iban}", iban)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.iban").value("DE89370400440532013000"))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.openingDate").value("01-01-2023"))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.absoluteLimit").value(100))
                .andExpect(jsonPath("$.transactionLimit").value(500))
                .andExpect(jsonPath("$.active").value(true));
    }

    private AccountDTO createTestAccountDTO() {
        return AccountDTO.builder()
                .id(UUID.randomUUID())
                .iban("DE89370400440532013000")
                .balance(new BigDecimal(1000))
                .accountType(AccountType.SAVINGS)
                .openingDate(LocalDate.of(2023, 1, 1))
                .userId(UUID.randomUUID())
                .absoluteLimit(new BigDecimal(100))
                .transactionLimit(new BigDecimal(500))
                .active(true)
                .build();
    }
}


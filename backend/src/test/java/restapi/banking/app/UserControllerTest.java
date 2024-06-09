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
import restapi.banking.app.controller.UserController;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.service.UserService;

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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper; // convert java object to json and vice versa

    /**
     * This method runs before each test
     * and prepare the mock object and set up mock mvc to
     * test the user controller with a fake user service
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testListOfUnapprovedUsers() throws Exception {
        // creates user list
        List<UserDTO> users = new ArrayList<>();
        users.add(createTestUserDTO());
        users.add(createTestUserDTO());

        //return the list of users and controls the behaviour of service layer
        given(userService.findUnapprovedUsers()).willReturn(users);

        /* send a get request with a content type of json
            and then check the response and compare the response containing
            2 items with each item having an id field
         */
        mockMvc.perform(get("/users/unapproved")
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
    @WithMockUser(roles = "EMPLOYEE")
    public void testListOfApprovedUsers() throws Exception {
        List<UserDTO> users = new ArrayList<>();
        users.add(createTestUserDTO());
        users.add(createTestUserDTO());

        given(userService.findApprovedUsersWithoutAccount()).willReturn(users);

        mockMvc.perform(get("/users/approved")
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
    @WithMockUser(roles = "EMPLOYEE")
    public void testApproveUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = createTestUserDTO();

        given(userService.approveUser(userId)).willReturn(userDTO);

        mockMvc.perform(post("/users/approve/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.approved").value(true))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 200) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testDeclineUser() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/users/decline/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User declined successfully."))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 200) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testGetAllUsers() throws Exception {
        List<UserDTO> users = new ArrayList<>();
        users.add(createTestUserDTO());
        users.add(createTestUserDTO());

        given(userService.getAllUsers()).willReturn(users);

        mockMvc.perform(get("/users")
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
    @WithMockUser(roles = "EMPLOYEE")
    public void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = createTestUserDTO();

        given(userService.findUserById(userId)).willReturn(userDTO);

        mockMvc.perform(get("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 200) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }


    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testUpdateDailyLimit() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = createTestUserDTO();
        userDTO.setDailyLimit(10000);

        given(userService.updateDailyLimit(eq(userId), any(Double.class))).willReturn(userDTO);

        mockMvc.perform(put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyLimit").value(10000))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 200) {
                        System.out.println("Error response: " + result.getResponse().getContentAsString());
                    }
                });
    }

    private UserDTO createTestUserDTO() {
        return UserDTO.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .bsn("123456789")
                .phone("1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .role(UserRole.Customer)
                .approved(true)
                .dailyLimit(5000)
                .build();
    }
}

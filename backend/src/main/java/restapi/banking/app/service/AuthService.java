package restapi.banking.app.service;

import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.LoginResponseDTO;
import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.exception.UserNotApprovedException;
import restapi.banking.app.model.User;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO register(RegistrationDTO registrationDTO) {
        validateRegistrationData(registrationDTO);
        User user = modelMapper.map(registrationDTO, User.class);
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(UserRole.Customer);
        user.setApproved(false);
        user.setDailyLimit(0);
        userRepository.saveAndFlush(user);
        return modelMapper.map(user, UserDTO.class);

    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // Use AuthenticationManager to authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // Get the authenticated user details and cast it to User
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = (User) userDetails;

            // Check if the user is approved
            if (!user.isApproved()) {
                throw new UserNotApprovedException(user.getFirstName()+ " "+ user.getLastName() +" is waiting for approval.");
            }

            String jwtToken = JwtService.generateToken(userDetails);

            LoginResponseDTO responseDTO = new LoginResponseDTO();
            responseDTO.setJwtToken(jwtToken);

            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            responseDTO.setUser(userDTO);

            return responseDTO;

        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password", ex);
        }
    }

    // private functions

    private void validateRegistrationData(RegistrationDTO registrationDTO) {
        doesEmailExist(registrationDTO.getEmail());
        isBsnValid(registrationDTO.getBsn());
        isUserAdult(registrationDTO.getDateOfBirth());
        isPhoneValid(registrationDTO.getPhone());
    }

    private void doesEmailExist(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private void isBsnValid(String bsn) {
        if (bsn == null || bsn.length() != 9 || !bsn.matches("[0-9]+")) {
            throw new IllegalArgumentException("BSN should contain only numbers and be 9 digits long");
        }

        int[] factors = { 9, 8, 7, 6, 5, 4, 3, 2, -1 };
        int checksum = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(bsn.charAt(i));
            checksum += digit * factors[i];
        }

        if (checksum % 11 != 0) {
            throw new IllegalArgumentException("Invalid BSN");
        }
    }

    private void isUserAdult(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.plusYears(18).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("User should be at least 18 years old");
        }
    }

    private void isPhoneValid(String phone) {
        if (phone == null || !phone.matches("[0-9]+") || phone.length() != 10) {
            throw new IllegalArgumentException("Phone number should contain only numbers and be 10 digits long");
        }
    }
}

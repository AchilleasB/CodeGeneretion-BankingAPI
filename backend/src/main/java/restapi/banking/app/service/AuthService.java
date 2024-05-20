package restapi.banking.app.service;

import restapi.banking.app.dto.LoginRequestDTO;
import restapi.banking.app.dto.LoginResponseDTO;
import restapi.banking.app.dto.RegistrationDTO;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.model.User;
import restapi.banking.app.model.UserRole;
import restapi.banking.app.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import javax.naming.AuthenticationException;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetails;
    @Autowired
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO register (RegistrationDTO registrationDTO) {
        doesEmailExist(registrationDTO.getEmail());
        isBsnValid(registrationDTO.getBsn());
        isUserAdult(registrationDTO.getDateOfBirth());
        User user = modelMapper.map(registrationDTO, User.class);
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(UserRole.Customer);
        user.setApproved(false);
        user.setDailyLimit(0);
        userRepository.saveAndFlush(user);
        return modelMapper.map(user, UserDTO.class);
        
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {
        UserDetails user = userDetails.loadUserByUsername(loginRequest.getEmail());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }
        
        LoginResponseDTO responseDTO = modelMapper.map(user, LoginResponseDTO.class);
        responseDTO.setJwtToken(JwtService.generateToken(user));
        return responseDTO;
    }


    // private functions
    // todo: Would be good to add throws to the functions here?
    private void doesEmailExist(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private void isBsnValid(String bsn) {
        if (bsn == null || bsn.length() != 9 || !bsn.matches("[0-9]+")) {
            throw new IllegalArgumentException("BSN should contain only numbers");
        }

        int[] factors = { 9, 8, 7, 6, 5, 4, 3, 2, -1 };
        int checksum = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(bsn.charAt(i));
            checksum += digit * factors[i];
        }

        if (checksum % 11 == 0)
            throw new IllegalArgumentException("BSN is not valid");
    }

    private void isUserAdult(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.plusYears(18).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("User should be at least 18 years old");
        }
    }
}

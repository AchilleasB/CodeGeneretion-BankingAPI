package restapi.banking.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static restapi.banking.app.model.UserRole.Employee;
import static restapi.banking.app.model.UserRole.Customer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/auth/register",
            "/auth/login"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
//     private final LogoutHandler logoutHandler;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                // .requestMatchers("/auth/**").hasAnyRole(Employee.name(), Customer.name())
                                // .requestMatchers(GET, "/management/**").hasAnyRole(Employee.name())
                                // .requestMatchers(POST, "/management/**").hasAnyRole(Employee.name())
                                // .requestMatchers(PUT, "/management/**").hasAnyRole(Employee.name())
                                // .requestMatchers(DELETE, "/management/**").hasAnyRole(Employee.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // .logout(logout ->
                //         logout.logoutUrl("/auth/logout")
                //                 .addLogoutHandler(logoutHandler)
                //                 .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                // )
        ;

        return http.build();
    }
}

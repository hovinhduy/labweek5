package iuh.fit.backend.configs;

import iuh.fit.backend.models.Company;
import iuh.fit.backend.services.CompanyService;
import iuh.fit.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CompanyService companyService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf ->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                         // Require authentication for upload
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/registerCandidate", "/login","/registerCompany").permitAll()
                        // Company URLs - Chỉ Company mới truy cập được
                        .requestMatchers("/company/**").hasRole("COMPANY")
                        // Candidate URLs - Chỉ Candidate mới truy cập được
                        .requestMatchers("/candidate/**").hasRole("CANDIDATE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Check if user is a company
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {

                // Check if company has completed setup
                Company company = companyService.getCompanyBy_Username(userDetails.getUsername());
                if (company == null) {
                    response.sendRedirect("/company/setup");
                } else {
                    response.sendRedirect("/company/job/post");
                }
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
                response.sendRedirect("/candidate/home");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
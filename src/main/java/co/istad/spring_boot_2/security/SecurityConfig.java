package co.istad.spring_boot_2.security;

import co.istad.spring_boot_2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        // create user admin
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(("admin"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails customer = User.builder()
//                .username("customer")
//                .password(passwordEncoder.encode("customer"))
//                .roles("CUSTOMER")
//                .build();
//
//        UserDetails staff = User.builder()
//                .username("staff")
//                .password(passwordEncoder.encode("staff"))
//                .roles("STUFF")
//                .build();
//
//        inMemoryUserDetailsManager.createUser(admin);
//        inMemoryUserDetailsManager.createUser(customer);
//        inMemoryUserDetailsManager.createUser(staff);
//        return inMemoryUserDetailsManager;
//    }

    // for DaoAuth
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;

        // spring version 3.5 up
        // DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService)
        // authProvider.setPasswordEncoder(passwordEncoder);
        // return authProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // allow and authentication request
        http.authorizeHttpRequests(auth -> auth
                // for customer
                        .requestMatchers(HttpMethod.POST,"/api/v1/customers/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/customers/**").hasAnyRole("STAFF", "ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/customers/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/customers/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/customers/**").hasAnyRole("ADMIN", "CUSTOMER")
                // for account
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts/**").hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts/**").hasAnyRole("ADMIN", "STAFF", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT,  "/api/v1/accounts/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/accounts/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/accounts/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .anyRequest().authenticated()
                );

        // disable login form in postman or api tester
        http.formLogin(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);

        // set security mechanism
        // Basic Auth (username, password)
        http.httpBasic(Customizer.withDefaults());

        // set to stateless session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

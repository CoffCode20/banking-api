package co.istad.spring_boot_2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class keycloakConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        Converter<Jwt, Collection<GrantedAuthority>> converter = jwt -> {

            Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
            Collection<String> authorities = realmAccess.get("roles");
            return authorities.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
        };
        JwtAuthenticationConverter jwtGrantedAuthoritiesConverter = new JwtAuthenticationConverter();
        jwtGrantedAuthoritiesConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtGrantedAuthoritiesConverter;
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

                .requestMatchers("/media/**", "/media/download/**").permitAll()
                .anyRequest().authenticated()
        );

        // disable login form in postman or api tester
        http.formLogin(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);

        // set security mechanism
        // Basic Auth (username, password)
        http.oauth2ResourceServer(oauth2 ->oauth2.jwt(Customizer.withDefaults()));

        // set to stateless session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

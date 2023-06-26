package ru.clevertec.employeeservice.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import ru.clevertec.employeeservice.security.JwtAuthConverter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthConverter jwtAuthConverter)  {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(policy -> policy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v0/employees/**"))
                            .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v0/employees/**"))
                            .hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/api/v0/employees/**"))
                            .hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/api/v0/employees/**"))
                            .hasRole("ADMIN")
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));

        return http.build();
    }

}

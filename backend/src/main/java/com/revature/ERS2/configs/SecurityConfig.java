package com.revature.ERS2.configs;

import com.revature.ERS2.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    //TODO: change requestMatchers to fit project requirements ex: only managers can access history, etc

    //NOTE: to disable JWT for easier testing, uncomment commented out lines of anyrequest.permitall and the ;
    //then comment out everything from .requestMatchers() ... authenticated() and also .addFilterBefore()
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/index.html",
                                    "/register.html"
                            ).permitAll()

                    .requestMatchers(HttpMethod.GET, "/api/reimbursements")
                        .hasAnyRole("EMPLOYEE", "MANAGER")
                    .requestMatchers(HttpMethod.GET, "/api/reimbursements/{id}")
                        .hasRole("MANAGER")
                    .requestMatchers(HttpMethod.GET, "/api/reimbursements/history")
                        .hasRole("MANAGER")

                    .requestMatchers(HttpMethod.POST, "/api/reimbursements")
                        .hasAnyRole("EMPLOYEE", "MANAGER")

                    .requestMatchers(HttpMethod.PATCH, "/api/reimbursements/{id}")
                        .hasAnyRole("EMPLOYEE", "MANAGER")
                    .requestMatchers(HttpMethod.PATCH, "/api/reimbursements/{id}/status")
                        .hasRole("MANAGER")

                    .requestMatchers(HttpMethod.DELETE, "/api/reimbursements/{id}")
                        .hasAnyRole("EMPLOYEE", "MANAGER")

                    .requestMatchers(HttpMethod.GET, "/api/users/me")
                        .hasAnyRole("EMPLOYEE", "MANAGER")

                    .requestMatchers(HttpMethod.GET, "/api/users/**")
                        .hasRole("MANAGER")

                    .requestMatchers(HttpMethod.GET, "/api/departments")
                        .permitAll()


                            .anyRequest().authenticated()

                    //.anyRequest().permitAll()
            ) //;
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
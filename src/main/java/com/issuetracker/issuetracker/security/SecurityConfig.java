package com.issuetracker.issuetracker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**The SecurityConfig class defines how incoming requests are authenticated and authorized:
 1.Some routes are explicitly accessible without authentication (e.g., /auth.txt/**).
 2.Other routes are protected.
 3.JWT tokens are checked before handling any other authentication mechanism.
 4.CORS and CSRF are configured appropriately for an API application.**/

@Configuration
@EnableWebSecurity //Enables Spring Security's web security support, which makes the application secure by default.
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true) //Enables security at the method level, such as by using the @Secured and @PreAuthorize annotations.
//`securedEnabled = true` allows the use of @Secured for role-based authorization.
public class SecurityConfig {

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider; //An authentication provider responsible for managing the actual authentication process

    //The main object that controls web security configurations.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Enables CORS (Cross-Origin Resource Sharing)
                .csrf(AbstractHttpConfigurer::disable) //Disables CSRF protection, which is typically useful when using sessionless state.
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                                )
                                .permitAll() //The patterns like /auth.txt/**, /swagger-ui/**, etc., are publicly accessible and don't require authentication.
                                .anyRequest()
                                .authenticated() //All other requests must be authenticated.
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) //Configures the application to not keep sessions between requests (using stateless JWT tokens).
                .authenticationProvider(authenticationProvider) //Uses the provided authentication provider to manage authentication.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //ensures that each request is validated with JWT authentication before handling username-password authentication.

        return http.build();
    }
}

package com.issuetracker.issuetracker.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration //defines beans that will be managed by the Spring IoC (Inversion of Control) container.
@RequiredArgsConstructor //it generates a constructor for the userDetailsService field.
public class BeansConfig {
    // declares a final field userDetailsService, which will be injected via constructor injection.
    //It's required for configuring the DaoAuthenticationProvider bean.
    private final UserDetailsService userDetailsService;

    //defines a bean named authenticationProvider.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        //configures a DaoAuthenticationProvider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //sets the userDetailsService
        authProvider.setUserDetailsService(userDetailsService);
        //configures the PasswordEncoder using the passwordEncoder()
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //retrieves the AuthenticationManager from the AuthenticationConfiguration
    //required for authentication purposes in Spring Security.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //encode and verify passwords securely
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //used for auditing purposes to determine the current auditor (e.g., user ID) during entity auditing.
    @Bean
    public AuditorAware<Integer> auditorAware() {
        return new ApplicationAuditAware();
    }

    //configured to allow cross-origin resource sharing (CORS) for requests
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(java.util.List.of("http://localhost:4200", "http://localhost:3000"));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));
        //action allowed
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "DELETE",
                "PUT",
                "PATCH"
        ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }

}

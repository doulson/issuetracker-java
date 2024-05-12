package com.issuetracker.issuetracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**The JwtFilter intercepts every HTTP request.
It extracts and validates the JWT token if present.
If authentication is successful, it sets the user in the security context to
ensure other parts of the application can identify and authorize the user correctly.**/

@Component //class as a Spring component so that Spring can detect it and register it as a bean
@RequiredArgsConstructor //Ensures that JwtService and UserDetailsService will be initialized
// provides a good starting point for any custom filter that should run once per request.
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService; //operations related to JWT, such as validating and extracting user details from tokens.
    private final UserDetailsService userDetailsService;

    //view Java Guide
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //if the request included auth.txt
        if (request.getServletPath().contains("/api/v1/auth.txt")) {
            filterChain.doFilter(request, response);
            return;
        }
        //if the request have header and defined Authorization Bearer Token
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //A chain of filters that needs to continue processing the request.
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        //The JwtService is used to extract the username (or email) from the token.
        userEmail = jwtService.extractUsername(jwt);
        //If the username is found and no authentication is already set in the SecurityContextHolder
        //SecurityContextHolder means that this user has not been authenticated yet for the current request.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                //If the token is valid, a UsernamePasswordAuthenticationToken is created with .
                //the user's authorities and set in the SecurityContextHolder.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

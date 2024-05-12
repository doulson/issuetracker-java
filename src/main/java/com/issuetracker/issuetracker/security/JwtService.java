package com.issuetracker.issuetracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The JwtService class provides methods to handle JWT operations:
 * Generating Tokens: Generates JWTs with or without extra claims and signs them.
 * Extracting Information: Retrieves specific claims or user information from tokens.
 * Validating Tokens: Checks that the token matches the user and isn’t expired.
 **/

@Service
//annotation marks the class as a Spring service component, allowing it to be automatically detected and registered as a bean for dependency injection.
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    //get value from resources/application-dev.yml
    @Value("${application.security.jwt.secret-key}")
    //inject configuration values into the fields from a properties or YAML file.
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    //Extracts the username (subject) from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //retrieves any claim from a token using a claimsResolver function,
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Generates a token using the UserDetails without extra claims, by calling the overloaded generateToken method with an empty map.
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    //Generates a token with extra claims. It delegates to buildToken, providing the configured expiration.
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        System.out.println(extraClaims.toString());
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    //Creates and signs a JWT using the provided claims, subject, and expiration time.
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        var authorities = userDetails.getAuthorities()
                .stream().
                map(GrantedAuthority::getAuthority)
                .toList();

        //Uses getSignInKey to get the signing key and signs the JWT.
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();
    }

    //Validates the token against a UserDetails object by comparing the username from the token
    //and //Checks that the token is not expired.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //Method for Checks that the token is not expired.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Extracts the expiration date from the token using the extractClaim method.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Parses the token using a signing key to verify the signature
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Converts the secretKey to bytes (after decoding from base64) and uses it to create an HMAC
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

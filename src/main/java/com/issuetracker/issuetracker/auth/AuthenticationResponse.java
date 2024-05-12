package com.issuetracker.issuetracker.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationResponse {
    private final String token;
}

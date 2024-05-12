package com.issuetracker.issuetracker.config;

import com.issuetracker.issuetracker.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * This ApplicationAuditAware class would typically be used in conjunction with Spring Data JPA's auditing capabilities.
 * You would configure Spring Data JPA to use this class as the auditor provider,
 * so that whenever an entity is created or modified, Spring Data would call getCurrentAuditor()
 * to determine the current auditor (i.e., the user responsible for the action).
 **/

public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();

        return Optional.ofNullable(userPrincipal.getId());
    }
}

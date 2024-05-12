package com.issuetracker.issuetracker.user;

import com.issuetracker.issuetracker.common.PageResponse;
import com.issuetracker.issuetracker.issue.Issue;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/token")
    public ResponseEntity<User> getUserByToken(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(((User) connectedUser.getPrincipal()));
    }

}

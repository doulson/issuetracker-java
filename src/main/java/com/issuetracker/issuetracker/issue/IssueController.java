package com.issuetracker.issuetracker.issue;

import com.issuetracker.issuetracker.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("issue")
@RequiredArgsConstructor
@Tag(name = "issue")
public class IssueController {

    private final IssueService service;

    @PostMapping
    public ResponseEntity<Integer> saveIssue(
            @Valid @RequestBody IssueRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(null, request, connectedUser));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Issue> getIssue(@PathVariable("id") Integer id, Authentication connectedUser) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<Issue>> getIssues(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllDisplayableIssue(page, size));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<Issue>> getIssuesByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllByOwner(page, size, connectedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateIssueById(
            @PathVariable("id") Integer id,
            @Valid @RequestBody IssueRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateIssue(id, request, connectedUser));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Integer> updateIssueStatusById(
            @PathVariable("id") Integer id,
            @Valid @RequestBody IssueRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateIssueStatus(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteIssueById(
            @PathVariable("id") Integer id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.deleteIssue(id, connectedUser));
    }

}

package com.issuetracker.issuetracker.issue_comment;

import com.issuetracker.issuetracker.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("issue-comment")
@RequiredArgsConstructor
@Tag(name = "issue-comment")
public class IssueCommentController {

    private final IssueCommentService service;

    @PostMapping
    public ResponseEntity<Integer> saveIssueComment(
            @Valid @RequestBody IssueCommentRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(null, request, connectedUser));
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<PageResponse<IssueCommentResponse>> getIssueCommentsByIssueId(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @PathVariable("issueId") Integer issueId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllByIssueId(page, size, issueId, connectedUser));
    }

    @GetMapping("/own")
    public ResponseEntity<PageResponse<IssueCommentResponse>> getIssueCommentsByUserId(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllByUserId(page, size, connectedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateIssueCommentById(
            @PathVariable("id") Integer id,
            @Valid @RequestBody IssueCommentRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateIssueComment(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteIssueCommentById(
            @PathVariable("id") Integer id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.deleteIssueComment(id, connectedUser));
    }
}

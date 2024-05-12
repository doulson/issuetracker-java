package com.issuetracker.issuetracker.issue_comment;

import jakarta.validation.constraints.NotBlank;

public record IssueCommentRequest(
        Integer id,
        @NotBlank(message = "100")
        String comment,
        Integer issueId
) {
}

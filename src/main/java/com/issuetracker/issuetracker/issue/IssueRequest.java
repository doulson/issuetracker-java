package com.issuetracker.issuetracker.issue;

import jakarta.validation.constraints.NotBlank;

public record IssueRequest(
        Integer id,
        @NotBlank(message = "100")
        String title,
        @NotBlank(message = "101")
        String description,
        Boolean archived,
        Status status
) {

}

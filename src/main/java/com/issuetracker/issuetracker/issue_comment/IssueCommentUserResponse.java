package com.issuetracker.issuetracker.issue_comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IssueCommentUserResponse {
    private Integer id;
    private String email;
    private String fullName;
}

package com.issuetracker.issuetracker.issue_comment;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueCommentResponse {
    private Integer id;
    private String comment;
    private Boolean ownComment;
    private IssueCommentUserResponse commentUser;
}

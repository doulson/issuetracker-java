package com.issuetracker.issuetracker.issue_comment;

import com.issuetracker.issuetracker.user.User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class IssueCommentMapper {

    public IssueComment toIssueComment(Integer id, IssueCommentRequest issueCommentRequest) {
        return IssueComment.builder()
                .id(id)
                .comment(issueCommentRequest.comment())
                .build();
    }

    public IssueCommentResponse toIssueCommentResponse(IssueComment issueComment, Integer id) {
        User user = issueComment.getUser();
        return IssueCommentResponse.builder()
                .id(issueComment.getId())
                .comment(issueComment.getComment())
                .ownComment(Objects.equals(issueComment.getCreatedBy(), id))
                .commentUser(IssueCommentUserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .build()
                )
                .build();
    }
}

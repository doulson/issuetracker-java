package com.issuetracker.issuetracker.issue_comment;

import org.springframework.data.jpa.domain.Specification;

public class IssueCommentSpecification {
    public static Specification<IssueComment> withIssueId(Integer issueId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("issue").get("id"), issueId);
    }

    public static Specification<IssueComment> withUserId(Integer issueId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), issueId);
    }
}

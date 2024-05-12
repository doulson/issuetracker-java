package com.issuetracker.issuetracker.issue;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class IssueSpecification {
    public static Specification<Issue> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Issue> displayable(Boolean display) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("archived"), !display);
    }
}

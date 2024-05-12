package com.issuetracker.issuetracker.issue_comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IssueCommentRepository extends JpaRepository<IssueComment, Integer>, JpaSpecificationExecutor<IssueComment> {

}

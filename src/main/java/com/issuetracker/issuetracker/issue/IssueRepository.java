package com.issuetracker.issuetracker.issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface IssueRepository extends JpaRepository<Issue, Integer>, JpaSpecificationExecutor<Issue> {

    @Query("""
            SELECT issue
            FROM Issue issue
            WHERE issue.archived = false
            AND issue.owner.id != :userId
            """)
    Page<Integer> findAllDisplayableIssues(Pageable pageable, Integer userId);
}

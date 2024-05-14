package com.issuetracker.issuetracker.issue_comment;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.issuetracker.issuetracker.common.BaseEntity;
import com.issuetracker.issuetracker.issue.Issue;
import com.issuetracker.issuetracker.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class IssueComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({"id", "fullName", "email"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonIncludeProperties({"id", "title", "status"})
    private Issue issue;


    // Constructors, getters, and setters
}

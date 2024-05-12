package com.issuetracker.issuetracker.issue;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.issuetracker.issuetracker.common.BaseEntity;
import com.issuetracker.issuetracker.issue_comment.IssueComment;
import com.issuetracker.issuetracker.issue_member.IssueMember;
import com.issuetracker.issuetracker.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Issue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private boolean archived;

    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIncludeProperties({"id", "fullName", "email"})
    private User owner;

    @JsonIgnore
    @OneToMany(mappedBy = "issue")
    private List<IssueMember> members;

    @JsonIgnore
    @OneToMany(mappedBy = "issue")
    private List<IssueComment> comments;

    @Transient
    public int getTotalComment() {
        return Optional.ofNullable(comments).map(List::size).orElse(0);
    }
}

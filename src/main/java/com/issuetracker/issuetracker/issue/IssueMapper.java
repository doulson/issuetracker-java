package com.issuetracker.issuetracker.issue;

import org.springframework.stereotype.Service;

@Service
public class IssueMapper {
    public Issue toIssue(Integer id, IssueRequest request) {
        return Issue.builder()
                .id(id)
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .archived(request.archived())
                .build();
    }


}

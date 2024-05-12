package com.issuetracker.issuetracker.issue;


import com.issuetracker.issuetracker.common.PageResponse;
import com.issuetracker.issuetracker.exception.BadRequestException;
import com.issuetracker.issuetracker.exception.ForbiddenException;
import com.issuetracker.issuetracker.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.issuetracker.issuetracker.issue.IssueSpecification.displayable;
import static com.issuetracker.issuetracker.issue.IssueSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IssueService {
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    public Integer save(Integer id, IssueRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Issue issue = issueMapper.toIssue(id, request);
        issue.setOwner(user);
        return issueRepository.save(issue).getId();
    }

    public Issue findById(Integer id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found with id: " + id));
    }

    public PageResponse<Issue> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Issue> issues = issueRepository.findAll(pageable);
        List<Issue> issueList = issues.stream().toList();
        return new PageResponse<>(
                issueList,
                issues.getNumber(),
                issues.getSize(),
                issues.getTotalElements(),
                issues.getTotalPages(),
                issues.isFirst(),
                issues.isLast()
        );
    }

    public PageResponse<Issue> findAllDisplayableIssue(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Issue> issues = issueRepository.findAll(displayable(true), pageable);
        List<Issue> issueList = issues.stream().toList();
        return new PageResponse<>(
                issueList,
                issues.getNumber(),
                issues.getSize(),
                issues.getTotalElements(),
                issues.getTotalPages(),
                issues.isFirst(),
                issues.isLast()
        );
    }

    public PageResponse<Issue> findAllByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Issue> issues = issueRepository.findAll(withOwnerId(user.getId()), pageable);
        List<Issue> issueList = issues.stream().toList();
        return new PageResponse<>(
                issueList,
                issues.getNumber(),
                issues.getSize(),
                issues.getTotalElements(),
                issues.getTotalPages(),
                issues.isFirst(),
                issues.isLast()
        );
    }

    public Integer updateIssue(int id, IssueRequest request, Authentication connectedUser) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found with id: " + id));
        User currentUser = (User) connectedUser.getPrincipal();
        if (issue.getOwner().getId().equals(currentUser.getId())) {
            return save(id, request, connectedUser);
        } else {
            throw new ForbiddenException("You are not allowed to update this issue");
        }
    }

    public Integer updateIssueStatus(Integer id, IssueRequest request, Authentication connectedUser) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found with id: " + id));
        User currentUser = (User) connectedUser.getPrincipal();
        if (issue.getOwner().getId().equals(currentUser.getId())) {
            issue.setStatus(request.status());
            return issueRepository.save(issue).getId();
        } else {
            throw new ForbiddenException("You are not allowed to update this issue");
        }
    }

    public Integer deleteIssue(Integer id, Authentication connectedUser) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found with id: " + id));
        User currentUser = (User) connectedUser.getPrincipal();
        if (issue.getOwner().getId().equals(currentUser.getId())) {
            issueRepository.deleteById(id);
            return issue.getId();
        } else {
            throw new ForbiddenException("You are not allowed to update this issue");
        }
    }
}

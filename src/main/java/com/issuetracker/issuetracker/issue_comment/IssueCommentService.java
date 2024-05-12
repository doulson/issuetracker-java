package com.issuetracker.issuetracker.issue_comment;

import com.issuetracker.issuetracker.common.PageResponse;
import com.issuetracker.issuetracker.exception.ForbiddenException;
import com.issuetracker.issuetracker.issue.Issue;
import com.issuetracker.issuetracker.issue.IssueRepository;
import com.issuetracker.issuetracker.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.issuetracker.issuetracker.issue_comment.IssueCommentSpecification.withIssueId;
import static com.issuetracker.issuetracker.issue_comment.IssueCommentSpecification.withUserId;

@Service
@RequiredArgsConstructor
public class IssueCommentService {
    private final IssueRepository issueRepository;
    private final IssueCommentRepository issueCommentRepository;
    private final IssueCommentMapper issueCommentMapper;

    public Integer save(Integer id, IssueCommentRequest request, Authentication connectedUser) {
        Issue issue = issueRepository.findById(request.issueId()).orElseThrow(() -> new EntityNotFoundException("No Issue found with ID:" + request.issueId()));
        User user = ((User) connectedUser.getPrincipal());
        IssueComment issueComment = issueCommentMapper.toIssueComment(id, request);
        issueComment.setUser(user);
        issueComment.setIssue(issue);
        return issueCommentRepository.save(issueComment).getId();
    }

    public PageResponse<IssueCommentResponse> findAllByIssueId(int page, int size, Integer issueId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<IssueComment> issueComments = issueCommentRepository.findAll(withIssueId(issueId), pageable);
        List<IssueCommentResponse> issueCommentResponses = issueComments.stream().
                map(c -> issueCommentMapper.toIssueCommentResponse(c, user.getId()))
                .toList();
        return new PageResponse<>(
                issueCommentResponses,
                issueComments.getNumber(),
                issueComments.getSize(),
                issueComments.getTotalElements(),
                issueComments.getTotalPages(),
                issueComments.isFirst(),
                issueComments.isLast()
        );
    }

    public PageResponse<IssueCommentResponse> findAllByUserId(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<IssueComment> issueComments = issueCommentRepository.findAll(withUserId(user.getId()), pageable);
        List<IssueCommentResponse> issueCommentResponses = issueComments.stream().
                map(c -> issueCommentMapper.toIssueCommentResponse(c, user.getId()))
                .toList();
        return new PageResponse<>(
                issueCommentResponses,
                issueComments.getNumber(),
                issueComments.getSize(),
                issueComments.getTotalElements(),
                issueComments.getTotalPages(),
                issueComments.isFirst(),
                issueComments.isLast()
        );
    }

    public Integer updateIssueComment(int id, IssueCommentRequest request, Authentication connectedUser) {
        IssueComment issueComment = issueCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found with id: " + id));
        User currentUser = (User) connectedUser.getPrincipal();
        if (issueComment.getUser().getId().equals(currentUser.getId())) {
            return save(id, request, connectedUser);
        } else {
            throw new ForbiddenException("You are not allowed to update this issue");
        }
    }

    public Integer deleteIssueComment(Integer id, Authentication connectedUser) {
        IssueComment issueComment = issueCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found with id: " + id));
        User currentUser = (User) connectedUser.getPrincipal();
        if (issueComment.getUser().getId().equals(currentUser.getId())) {
            issueCommentRepository.deleteById(id);
            return issueComment.getId();
        } else {
            throw new ForbiddenException("You are not allowed to update this issue");
        }
    }
}

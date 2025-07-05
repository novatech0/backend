package com.agrotech.api.forum.application.internal.commandservices;

import com.agrotech.api.forum.domain.exceptions.ForumPostNotFoundException;
import com.agrotech.api.forum.domain.exceptions.ForumReplyNotFoundException;
import com.agrotech.api.forum.domain.model.commands.CreateForumReplyCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumReplyCommand;
import com.agrotech.api.forum.domain.model.entities.ForumReply;
import com.agrotech.api.forum.domain.services.ForumReplyCommandService;
import com.agrotech.api.forum.infrastructure.persistence.jpa.mappers.ForumPostMapper;
import com.agrotech.api.forum.infrastructure.persistence.jpa.mappers.ForumReplyMapper;
import com.agrotech.api.forum.infrastructure.persistence.jpa.repositories.ForumPostRepository;
import com.agrotech.api.forum.infrastructure.persistence.jpa.repositories.ForumReplyRepository;
import com.agrotech.api.profile.application.internal.outboundservices.acl.ExternalUserService;
import com.agrotech.api.shared.domain.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ForumReplyCommandServiceImpl implements ForumReplyCommandService {

    private final ForumReplyRepository forumReplyRepository;
    private final ForumPostRepository forumPostRepository;
    private final ExternalUserService externalUserService;

    public ForumReplyCommandServiceImpl(ForumReplyRepository forumReplyRepository,
                                        ForumPostRepository forumPostRepository,
                                        ExternalUserService externalUserService) {
        this.forumReplyRepository = forumReplyRepository;
        this.forumPostRepository = forumPostRepository;
        this.externalUserService = externalUserService;
    }

    @Override
    public Long handle(CreateForumReplyCommand command) {
        var user = externalUserService.fetchUserById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));

        var postEntity = forumPostRepository.findById(command.forumPostId())
                .orElseThrow(() -> new ForumPostNotFoundException(command.forumPostId()));

        var post = ForumReply.create(command, ForumPostMapper.toDomain(postEntity), user);
        var replyEntity = ForumReplyMapper.toEntity(post);
        var saved = forumReplyRepository.save(replyEntity);
        return saved.getId();
    }

    @Override
    public void handle(DeleteForumReplyCommand command) {
        var reply = forumReplyRepository.findById(command.id())
                .orElseThrow(() -> new ForumReplyNotFoundException(command.id()));
        forumReplyRepository.delete(reply);
    }
}

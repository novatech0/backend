package com.agrotech.api.forum.application.internal.commandservices;

import com.agrotech.api.forum.domain.exceptions.ForumPostNotFoundException;
import com.agrotech.api.forum.domain.model.aggregates.ForumPost;
import com.agrotech.api.forum.domain.model.commands.CreateForumPostCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumPostCommand;
import com.agrotech.api.forum.domain.services.ForumPostCommandService;
import com.agrotech.api.forum.infrastructure.persistence.jpa.mappers.ForumPostMapper;
import com.agrotech.api.forum.infrastructure.persistence.jpa.repositories.ForumPostRepository;
import com.agrotech.api.profile.application.internal.outboundservices.acl.ExternalUserService;
import com.agrotech.api.shared.domain.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ForumPostCommandServiceImpl implements ForumPostCommandService {

    private final ForumPostRepository forumPostRepository;
    private final ExternalUserService externalUserService;

    public ForumPostCommandServiceImpl(ForumPostRepository forumPostRepository, ExternalUserService externalUserService) {
        this.forumPostRepository = forumPostRepository;
        this.externalUserService = externalUserService;
    }

    @Override
    public Long handle(CreateForumPostCommand command) {
        var user = externalUserService.fetchUserById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        var post = ForumPost.create(command, user);
        var savedEntity = forumPostRepository.save(ForumPostMapper.toEntity(post));
        return savedEntity.getId();
    }

    @Override
    public void handle(DeleteForumPostCommand command) {
        var postEntity = forumPostRepository.findById(command.id())
                .orElseThrow(() -> new ForumPostNotFoundException(command.id()));
        forumPostRepository.delete(postEntity);
    }
}
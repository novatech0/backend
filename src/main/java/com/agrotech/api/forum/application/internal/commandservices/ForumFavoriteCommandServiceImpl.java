package com.agrotech.api.forum.application.internal.commandservices;

import com.agrotech.api.forum.domain.exceptions.ForumFavoriteNotFoundException;
import com.agrotech.api.forum.domain.exceptions.ForumPostNotFoundException;
import com.agrotech.api.forum.domain.model.commands.CreateForumFavoriteCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumFavoriteCommand;
import com.agrotech.api.forum.domain.model.entities.ForumFavorite;
import com.agrotech.api.forum.domain.services.ForumFavoriteCommandService;
import com.agrotech.api.forum.infrastructure.persistence.jpa.mappers.ForumFavoriteMapper;
import com.agrotech.api.forum.infrastructure.persistence.jpa.mappers.ForumPostMapper;
import com.agrotech.api.forum.infrastructure.persistence.jpa.repositories.ForumFavoriteRepository;
import com.agrotech.api.forum.infrastructure.persistence.jpa.repositories.ForumPostRepository;
import com.agrotech.api.profile.application.internal.outboundservices.acl.ExternalUserService;
import com.agrotech.api.shared.domain.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ForumFavoriteCommandServiceImpl implements ForumFavoriteCommandService {

    private final ForumFavoriteRepository favoriteRepository;
    private final ForumPostRepository postRepository;
    private final ExternalUserService externalUserService;

    public ForumFavoriteCommandServiceImpl(
            ForumFavoriteRepository favoriteRepository,
            ForumPostRepository postRepository,
            ExternalUserService externalUserService) {
        this.favoriteRepository = favoriteRepository;
        this.postRepository = postRepository;
        this.externalUserService = externalUserService;
    }

    @Override
    public Long handle(CreateForumFavoriteCommand command) {
        var user = externalUserService.fetchUserById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));

        var forumPost = postRepository.findById(command.forumPostId())
                .orElseThrow(() -> new ForumPostNotFoundException(command.forumPostId()));

        var favorite = ForumFavorite.create(user, ForumPostMapper.toDomain(forumPost));
        var saved = favoriteRepository.save(ForumFavoriteMapper.toEntity(favorite));
        return saved.getId();
    }

    @Override
    public void handle(DeleteForumFavoriteCommand command) {
        var favorite = favoriteRepository
                .findByUser_IdAndForumPost_Id(command.userId(), command.forumPostId())
                .orElseThrow(() -> new ForumFavoriteNotFoundException(command.userId(), command.forumPostId()));
        favoriteRepository.delete(favorite);
    }
}

package com.agrotech.api.forum.application.internal.queryservices;

import com.agrotech.api.forum.domain.model.entities.ForumFavorite;
import com.agrotech.api.forum.domain.model.queries.CheckForumFavoriteExistsQuery;
import com.agrotech.api.forum.domain.model.queries.GetAllForumFavoritesByForumPostIdQuery;
import com.agrotech.api.forum.domain.model.queries.GetAllForumFavoritesByUserIdQuery;
import com.agrotech.api.forum.domain.services.ForumFavoriteQueryService;
import com.agrotech.api.forum.infrastructure.persistence.jpa.mappers.ForumFavoriteMapper;
import com.agrotech.api.forum.infrastructure.persistence.jpa.repositories.ForumFavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumFavoriteQueryServiceImpl implements ForumFavoriteQueryService {

    private final ForumFavoriteRepository favoriteRepository;

    public ForumFavoriteQueryServiceImpl(ForumFavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public List<ForumFavorite> handle(GetAllForumFavoritesByForumPostIdQuery query) {
        return favoriteRepository.findAllByForumPost_Id(query.forumPostId())
                .stream()
                .map(ForumFavoriteMapper::toDomain)
                .toList();
    }

    @Override
    public List<ForumFavorite> handle(GetAllForumFavoritesByUserIdQuery query) {
        return favoriteRepository.findAllByUser_Id(query.userId())
                .stream()
                .map(ForumFavoriteMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ForumFavorite> handle(CheckForumFavoriteExistsQuery query) {
        return favoriteRepository.findByUser_IdAndForumPost_Id(query.userId(), query.forumPostId())
                .map(ForumFavoriteMapper::toDomain);
    }
}

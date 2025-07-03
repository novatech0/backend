package com.agrotech.api.forum.domain.services;

import com.agrotech.api.forum.domain.model.entities.ForumFavorite;
import com.agrotech.api.forum.domain.model.queries.CheckForumFavoriteExistsQuery;
import com.agrotech.api.forum.domain.model.queries.GetAllForumFavoritesByForumPostIdQuery;
import com.agrotech.api.forum.domain.model.queries.GetAllForumFavoritesByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface ForumFavoriteQueryService {
    List<ForumFavorite> handle(GetAllForumFavoritesByForumPostIdQuery query);
    List<ForumFavorite> handle(GetAllForumFavoritesByUserIdQuery query);
    Optional<ForumFavorite> handle(CheckForumFavoriteExistsQuery query);
}

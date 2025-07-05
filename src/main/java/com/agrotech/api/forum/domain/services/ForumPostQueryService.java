package com.agrotech.api.forum.domain.services;

import com.agrotech.api.forum.domain.model.aggregates.ForumPost;
import com.agrotech.api.forum.domain.model.queries.GetAllForumPostsQuery;
import com.agrotech.api.forum.domain.model.queries.GetForumPostByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ForumPostQueryService {
    List<ForumPost> handle(GetAllForumPostsQuery query);
    Optional<ForumPost> handle(GetForumPostByIdQuery query);
}

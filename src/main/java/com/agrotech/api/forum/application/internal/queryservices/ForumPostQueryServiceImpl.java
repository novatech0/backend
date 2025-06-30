package com.agrotech.api.forum.application.internal.queryservices;

import com.agrotech.api.forum.domain.model.aggregates.ForumPost;
import com.agrotech.api.forum.domain.model.queries.GetAllForumPostsQuery;
import com.agrotech.api.forum.domain.model.queries.GetForumPostByIdQuery;
import com.agrotech.api.forum.domain.services.ForumPostQueryService;
import com.agrotech.api.forum.infrastructure.persistence.jpa.mappers.ForumPostMapper;
import com.agrotech.api.forum.infrastructure.persistence.jpa.repositories.ForumPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumPostQueryServiceImpl implements ForumPostQueryService {

    private final ForumPostRepository forumPostRepository;

    public ForumPostQueryServiceImpl(ForumPostRepository forumPostRepository) {
        this.forumPostRepository = forumPostRepository;
    }

    @Override
    public List<ForumPost> handle(GetAllForumPostsQuery query) {
        return forumPostRepository.findAll()
                .stream()
                .map(ForumPostMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ForumPost> handle(GetForumPostByIdQuery query) {
        return forumPostRepository.findById(query.id())
                .map(ForumPostMapper::toDomain);
    }
}
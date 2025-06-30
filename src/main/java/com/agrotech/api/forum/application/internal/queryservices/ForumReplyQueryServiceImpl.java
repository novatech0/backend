package com.agrotech.api.forum.application.internal.queryservices;

import com.agrotech.api.forum.domain.model.entities.ForumReply;
import com.agrotech.api.forum.domain.model.queries.GetAllForumRepliesFromForumPostIdQuery;
import com.agrotech.api.forum.domain.services.ForumReplyQueryService;
import com.agrotech.api.forum.infrastructure.persistence.jpa.mappers.ForumReplyMapper;
import com.agrotech.api.forum.infrastructure.persistence.jpa.repositories.ForumReplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumReplyQueryServiceImpl implements ForumReplyQueryService {

    private final ForumReplyRepository forumReplyRepository;

    public ForumReplyQueryServiceImpl(ForumReplyRepository forumReplyRepository) {
        this.forumReplyRepository = forumReplyRepository;
    }

    @Override
    public List<ForumReply> handle(GetAllForumRepliesFromForumPostIdQuery query) {
        return forumReplyRepository.findAllByForumPost_Id(query.forumPostId())
                .stream()
                .map(ForumReplyMapper::toDomain)
                .toList();
    }
}

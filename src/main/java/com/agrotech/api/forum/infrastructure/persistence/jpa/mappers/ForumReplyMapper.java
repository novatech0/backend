package com.agrotech.api.forum.infrastructure.persistence.jpa.mappers;

import com.agrotech.api.forum.domain.model.entities.ForumReply;
import com.agrotech.api.forum.infrastructure.persistence.jpa.entities.ForumReplyEntity;
import com.agrotech.api.iam.infrastructure.persistence.jpa.mappers.UserMapper;

public class ForumReplyMapper {
    public static ForumReply toDomain(ForumReplyEntity entity) {
        if (entity == null) return null;
        return new ForumReply(
                entity.getId(),
                entity.getContent(),
                ForumPostMapper.toDomain(entity.getForumPost()),
                UserMapper.toDomain(entity.getUser())
        );
    }

    public static ForumReplyEntity toEntity(ForumReply domain) {
        if (domain == null) return null;
        return ForumReplyEntity.builder()
                .id(domain.getId())
                .content(domain.getContent())
                .forumPost(ForumPostMapper.toEntity(domain.getForumPost()))
                .user(UserMapper.toEntity(domain.getUser()))
                .build();
    }
}

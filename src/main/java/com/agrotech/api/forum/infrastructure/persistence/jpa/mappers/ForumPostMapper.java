package com.agrotech.api.forum.infrastructure.persistence.jpa.mappers;

import com.agrotech.api.forum.domain.model.aggregates.ForumPost;
import com.agrotech.api.forum.infrastructure.persistence.jpa.entities.ForumPostEntity;
import com.agrotech.api.iam.infrastructure.persistence.jpa.mappers.UserMapper;

public class ForumPostMapper {

    public static ForumPost toDomain(ForumPostEntity entity) {
        if (entity == null) return null;
        return new ForumPost(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                UserMapper.toDomain(entity.getUser())
        );
    }

    public static ForumPostEntity toEntity(ForumPost domain) {
        if (domain == null) return null;
        return ForumPostEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .content(domain.getContent())
                .user(UserMapper.toEntity(domain.getUser()))
                .build();
    }
}
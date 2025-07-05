package com.agrotech.api.forum.infrastructure.persistence.jpa.mappers;

import com.agrotech.api.forum.domain.model.entities.ForumFavorite;
import com.agrotech.api.forum.infrastructure.persistence.jpa.entities.ForumFavoriteEntity;
import com.agrotech.api.iam.infrastructure.persistence.jpa.mappers.UserMapper;

public class ForumFavoriteMapper {

    public static ForumFavorite toDomain(ForumFavoriteEntity entity) {
        if (entity == null) return null;
        return new ForumFavorite(
                entity.getId(),
                UserMapper.toDomain(entity.getUser()),
                ForumPostMapper.toDomain(entity.getForumPost())
        );
    }

    public static ForumFavoriteEntity toEntity(ForumFavorite domain) {
        if (domain == null) return null;
        return ForumFavoriteEntity.builder()
                .id(domain.getId())
                .user(UserMapper.toEntity(domain.getUser()))
                .forumPost(ForumPostMapper.toEntity(domain.getForumPost()))
                .build();
    }
}

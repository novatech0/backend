package com.agrotech.api.post.infrastructure.persistence.jpa.mappers;

import com.agrotech.api.post.domain.model.aggregates.Post;
import com.agrotech.api.post.domain.model.commands.CreatePostCommand;
import com.agrotech.api.post.infrastructure.persistence.jpa.entities.PostEntity;
import com.agrotech.api.profile.domain.model.entities.Advisor;

public class PostMapper {
    public static Post toDomain(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getImage(),
                entity.getAdvisorId()
        );
    }

    public static PostEntity toEntity(CreatePostCommand command, Advisor advisor) {
        return new PostEntity(command, advisor);
    }
}

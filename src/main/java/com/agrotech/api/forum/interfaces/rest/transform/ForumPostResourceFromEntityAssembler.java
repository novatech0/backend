package com.agrotech.api.forum.interfaces.rest.transform;

import com.agrotech.api.forum.domain.model.aggregates.ForumPost;
import com.agrotech.api.forum.interfaces.rest.resources.ForumPostResource;

public class ForumPostResourceFromEntityAssembler {
    public static ForumPostResource toResourceFromEntity(ForumPost entity) {
        return new ForumPostResource(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getContent()
        );
    }
}

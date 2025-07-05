package com.agrotech.api.forum.interfaces.rest.transform;

import com.agrotech.api.forum.domain.model.entities.ForumFavorite;
import com.agrotech.api.forum.interfaces.rest.resources.ForumFavoriteResource;

public class ForumFavoriteResourceFromEntityAssembler {
    public static ForumFavoriteResource toResourceFromEntity(ForumFavorite entity) {
        return new ForumFavoriteResource(
                entity.getId(),
                entity.getUserId(),
                entity.getForumPostId()
        );
    }
}

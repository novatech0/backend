package com.agrotech.api.forum.interfaces.rest.transform;

import com.agrotech.api.forum.domain.model.commands.CreateForumFavoriteCommand;
import com.agrotech.api.forum.interfaces.rest.resources.CreateForumFavoriteResource;

public class CreateForumFavoriteCommandFromResourceAssembler {
    public static CreateForumFavoriteCommand toCommandFromResource(CreateForumFavoriteResource resource) {
        return new CreateForumFavoriteCommand(resource.userId(), resource.forumPostId());
    }
}

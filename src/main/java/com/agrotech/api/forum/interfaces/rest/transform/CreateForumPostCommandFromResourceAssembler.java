package com.agrotech.api.forum.interfaces.rest.transform;

import com.agrotech.api.forum.domain.model.commands.CreateForumPostCommand;
import com.agrotech.api.forum.interfaces.rest.resources.CreateForumPostResource;

public class CreateForumPostCommandFromResourceAssembler {
    public static CreateForumPostCommand toCommandFromResource(CreateForumPostResource resource) {
        return new CreateForumPostCommand(
                resource.userId(),
                resource.title(),
                resource.content()
        );
    }
}

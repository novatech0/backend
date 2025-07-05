package com.agrotech.api.forum.interfaces.rest.transform;

import com.agrotech.api.forum.domain.model.commands.CreateForumReplyCommand;
import com.agrotech.api.forum.interfaces.rest.resources.CreateForumReplyResource;

public class CreateForumReplyCommandFromResourceAssembler {
    public static CreateForumReplyCommand toCommandFromResource(CreateForumReplyResource resource) {
        return new CreateForumReplyCommand(
                resource.userId(),
                resource.forumPostId(),
                resource.content()
        );
    }
}

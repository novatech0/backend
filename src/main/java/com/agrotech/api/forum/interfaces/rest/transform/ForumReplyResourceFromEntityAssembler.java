package com.agrotech.api.forum.interfaces.rest.transform;

import com.agrotech.api.forum.domain.model.entities.ForumReply;
import com.agrotech.api.forum.interfaces.rest.resources.ForumReplyResource;

public class ForumReplyResourceFromEntityAssembler {
    public static ForumReplyResource toResourceFromEntity(ForumReply entity) {
        return new ForumReplyResource(
                entity.getId(),
                entity.getUserId(),
                entity.getForumPostId(),
                entity.getContent()
        );
    }
}

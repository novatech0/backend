package com.agrotech.api.forum.domain.services;

import com.agrotech.api.forum.domain.model.commands.CreateForumPostCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumPostCommand;

public interface ForumPostCommandService {
    Long handle(CreateForumPostCommand command);
    void handle(DeleteForumPostCommand command);
}

package com.agrotech.api.forum.domain.services;

import com.agrotech.api.forum.domain.model.commands.CreateForumReplyCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumReplyCommand;

public interface ForumReplyCommandService {
    Long handle(CreateForumReplyCommand command);
    void handle(DeleteForumReplyCommand command);
}

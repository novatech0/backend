package com.agrotech.api.forum.domain.services;

import com.agrotech.api.forum.domain.model.commands.CreateForumFavoriteCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumFavoriteCommand;

public interface ForumFavoriteCommandService {
    Long handle(CreateForumFavoriteCommand command);
    void handle(DeleteForumFavoriteCommand command);
}

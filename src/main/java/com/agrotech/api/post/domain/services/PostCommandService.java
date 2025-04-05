package com.agrotech.api.post.domain.services;

import com.agrotech.api.post.domain.model.aggregates.Post;
import com.agrotech.api.post.domain.model.commands.CreatePostCommand;
import com.agrotech.api.post.domain.model.commands.DeletePostCommand;
import com.agrotech.api.post.domain.model.commands.UpdatePostCommand;

import java.util.Optional;

public interface PostCommandService {
    Long handle(CreatePostCommand command);
    Optional<Post> handle(UpdatePostCommand command);
    void handle(DeletePostCommand command);
}

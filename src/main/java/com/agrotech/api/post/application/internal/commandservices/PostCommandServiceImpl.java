package com.agrotech.api.post.application.internal.commandservices;

import com.agrotech.api.post.application.internal.outboundservices.acl.ExternalProfileService;
import com.agrotech.api.shared.domain.exceptions.AdvisorNotFoundException;
import com.agrotech.api.post.domain.exceptions.PostNotFoundException;
import com.agrotech.api.post.domain.model.aggregates.Post;
import com.agrotech.api.post.domain.model.commands.CreatePostCommand;
import com.agrotech.api.post.domain.model.commands.DeletePostCommand;
import com.agrotech.api.post.domain.model.commands.UpdatePostCommand;
import com.agrotech.api.post.domain.services.PostCommandService;
import com.agrotech.api.post.infrastructure.persistence.jpa.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostCommandServiceImpl implements PostCommandService {
    private final PostRepository postRepository;
    private final ExternalProfileService externalProfileService;

    public PostCommandServiceImpl(PostRepository postRepository, ExternalProfileService externalProfileService) {
        this.postRepository = postRepository;
        this.externalProfileService = externalProfileService;
    }

    @Override
    public Long handle(CreatePostCommand command) {
        var advisor = externalProfileService.fetchAdvisorById(command.advisorId())
                .orElseThrow(() -> new AdvisorNotFoundException(command.advisorId()));
        Post post = new Post(command, advisor);
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public Optional<Post> handle(UpdatePostCommand command) {
        var post = postRepository.findById(command.id())
                .orElseThrow(() -> new PostNotFoundException(command.id()));
        Post updatedPost = postRepository.save(post.update(command));
        return Optional.of(updatedPost);
    }

    @Override
    public void handle(DeletePostCommand command) {
        var post = postRepository.findById(command.id())
                .orElseThrow(() -> new PostNotFoundException(command.id()));
        postRepository.delete(post);
    }

}

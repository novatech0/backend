package com.agrotech.api.post.application.internal.commandservices;

import com.agrotech.api.appointment.infrastructure.persistence.jpa.mappers.AppointmentMapper;
import com.agrotech.api.post.application.internal.outboundservices.acl.ExternalProfileService;
import com.agrotech.api.post.infrastructure.persistence.jpa.entities.PostEntity;
import com.agrotech.api.post.infrastructure.persistence.jpa.mappers.PostMapper;
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
        var advisor = externalProfileService.fetchAdvisorById(command.advisorId());
        if (advisor.isEmpty()) throw new AdvisorNotFoundException(command.advisorId());

        var postEntity = PostMapper.toEntity(command, advisor.get());
        postRepository.save(postEntity);
        return postEntity.getId();
    }

    @Override
    public Optional<Post> handle(UpdatePostCommand command) {
        var post = postRepository.findById(command.id());
        if (post.isEmpty()) throw new PostNotFoundException(command.id());

        var postToUpdate = post.get().update(command);
        PostEntity updatedPost = postRepository.save(postToUpdate);
        return Optional.of(PostMapper.toDomain(updatedPost));
    }

    @Override
    public void handle(DeletePostCommand command) {
        var post = postRepository.findById(command.id());
        if (post.isEmpty()) throw new PostNotFoundException(command.id());

        postRepository.delete(post.get());
    }

}

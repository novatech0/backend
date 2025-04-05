package com.agrotech.api.post.application.internal.queryservices;

import com.agrotech.api.post.domain.model.aggregates.Post;
import com.agrotech.api.post.domain.model.queries.GetAllPostsQuery;
import com.agrotech.api.post.domain.model.queries.GetPostByAdvisorIdQuery;
import com.agrotech.api.post.domain.model.queries.GetPostByIdQuery;
import com.agrotech.api.post.domain.services.PostQueryService;
import com.agrotech.api.post.infrastructure.persistence.jpa.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostQueryServiceImpl implements PostQueryService {
    private final PostRepository postRepository;

    public PostQueryServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> handle(GetAllPostsQuery query) {
        return postRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public Optional<Post> handle(GetPostByIdQuery query) {
        return postRepository.findById(query.id());
    }

    @Override
    public List<Post> handle(GetPostByAdvisorIdQuery query) {
        return postRepository.findByAdvisor_Id(query.advisorId());
    }
}

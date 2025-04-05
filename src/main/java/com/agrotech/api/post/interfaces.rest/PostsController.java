package com.agrotech.api.post.interfaces.rest;

import com.agrotech.api.post.domain.model.aggregates.Post;
import com.agrotech.api.post.domain.model.commands.DeletePostCommand;
import com.agrotech.api.post.domain.model.queries.GetAllPostsQuery;
import com.agrotech.api.post.domain.model.queries.GetPostByAdvisorIdQuery;
import com.agrotech.api.post.domain.model.queries.GetPostByIdQuery;
import com.agrotech.api.post.domain.services.PostCommandService;
import com.agrotech.api.post.domain.services.PostQueryService;
import com.agrotech.api.post.interfaces.rest.resources.CreatePostResource;
import com.agrotech.api.post.interfaces.rest.resources.PostResource;
import com.agrotech.api.post.interfaces.rest.resources.UpdatePostResource;
import com.agrotech.api.post.interfaces.rest.transform.CreatePostCommandFromResourceAssembler;
import com.agrotech.api.post.interfaces.rest.transform.PostResourceFromEntityAssembler;
import com.agrotech.api.post.interfaces.rest.transform.UpdatePostCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value="api/v1/posts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Posts", description = "Post Management Endpoints")
public class PostsController {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    public PostsController(PostCommandService postCommandService, PostQueryService postQueryService) {
        this.postCommandService = postCommandService;
        this.postQueryService = postQueryService;
    }

    @GetMapping
    public ResponseEntity<List<PostResource>> getPosts(
            @RequestParam(value = "advisorId", required = false) Long advisorId) {

        List<Post> posts;

        if (advisorId != null) {
            var getPostsByAdvisorIdQuery = new GetPostByAdvisorIdQuery(advisorId);
            posts = postQueryService.handle(getPostsByAdvisorIdQuery);
        } else {
            var getAllPostsQuery = new GetAllPostsQuery();
            posts = postQueryService.handle(getAllPostsQuery);
        }

        var postResources = posts.stream()
                .map(PostResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(postResources);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostResource> getPostById(@PathVariable Long id) {
        var getPostByIdQuery = new GetPostByIdQuery(id);
        var post = postQueryService.handle(getPostByIdQuery);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var postResource = PostResourceFromEntityAssembler.toResourceFromEntity(post.get());
        return ResponseEntity.ok(postResource);
    }

    @PostMapping
    public ResponseEntity<PostResource> createPost(@RequestBody CreatePostResource createPostResource) {
        var createPostCommand = CreatePostCommandFromResourceAssembler.toCommandFromResource(createPostResource);
        var postId = postCommandService.handle(createPostCommand);
        if (postId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getPostByIdQuery = new GetPostByIdQuery(postId);
        var post = postQueryService.handle(getPostByIdQuery);
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var postResource = PostResourceFromEntityAssembler.toResourceFromEntity(post.get());
        return new ResponseEntity<>(postResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResource> updatePost(@PathVariable Long id, @RequestBody UpdatePostResource updatePostResource) {
        var updatePostCommand = UpdatePostCommandFromResourceAssembler.toCommandFromResource(id, updatePostResource);
        Optional<Post> post;
        try {
            post = postCommandService.handle(updatePostCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var postResource = PostResourceFromEntityAssembler.toResourceFromEntity(post.get());
        return ResponseEntity.ok(postResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        var deletePostCommand = new DeletePostCommand(id);
        try{
            postCommandService.handle(deletePostCommand);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok("Post with id " + id + " successfully deleted");
    }
}
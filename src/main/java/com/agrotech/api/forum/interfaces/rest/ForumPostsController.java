package com.agrotech.api.forum.interfaces.rest;

import com.agrotech.api.forum.domain.model.commands.CreateForumPostCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumPostCommand;
import com.agrotech.api.forum.domain.model.queries.GetAllForumPostsQuery;
import com.agrotech.api.forum.domain.model.queries.GetForumPostByIdQuery;
import com.agrotech.api.forum.domain.services.ForumPostCommandService;
import com.agrotech.api.forum.domain.services.ForumPostQueryService;
import com.agrotech.api.forum.interfaces.rest.resources.CreateForumPostResource;
import com.agrotech.api.forum.interfaces.rest.resources.ForumPostResource;
import com.agrotech.api.forum.interfaces.rest.transform.CreateForumPostCommandFromResourceAssembler;
import com.agrotech.api.forum.interfaces.rest.transform.ForumPostResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RestController
@RequestMapping(value = "api/v1/forum-posts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ForumPosts", description = "Forum Post Management Endpoints")
public class ForumPostsController {
    private final ForumPostCommandService forumPostCommandService;
    private final ForumPostQueryService forumPostQueryService;

    public ForumPostsController(ForumPostCommandService forumPostCommandService,
                                ForumPostQueryService forumPostQueryService) {
        this.forumPostCommandService = forumPostCommandService;
        this.forumPostQueryService = forumPostQueryService;
    }

    @GetMapping
    public ResponseEntity<List<ForumPostResource>> getAllForumPosts() {
        var query = new GetAllForumPostsQuery();
        var forumPosts = forumPostQueryService.handle(query);
        var resources = forumPosts.stream()
                .map(ForumPostResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumPostResource> getForumPostById(@PathVariable Long id) {
        var query = new GetForumPostByIdQuery(id);
        var forumPost = forumPostQueryService.handle(query);
        if (forumPost.isEmpty()) return ResponseEntity.notFound().build();
        var resource = ForumPostResourceFromEntityAssembler.toResourceFromEntity(forumPost.get());
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<ForumPostResource> createForumPost(@RequestBody CreateForumPostResource resource) {
        CreateForumPostCommand command = CreateForumPostCommandFromResourceAssembler.toCommandFromResource(resource);
        Long postId = forumPostCommandService.handle(command);
        var query = new GetForumPostByIdQuery(postId);
        var forumPost = forumPostQueryService.handle(query);
        if (forumPost.isEmpty()) return ResponseEntity.badRequest().build();
        var postResource = ForumPostResourceFromEntityAssembler.toResourceFromEntity(forumPost.get());
        return new ResponseEntity<>(postResource, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteForumPost(@PathVariable Long id) {
        var command = new DeleteForumPostCommand(id);
        forumPostCommandService.handle(command);
        return ResponseEntity.ok("Forum post with id " + id + " successfully deleted");
    }
}

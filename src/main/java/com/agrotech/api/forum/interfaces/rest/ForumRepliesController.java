package com.agrotech.api.forum.interfaces.rest;

import com.agrotech.api.forum.domain.model.commands.CreateForumReplyCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumReplyCommand;
import com.agrotech.api.forum.domain.model.entities.ForumReply;
import com.agrotech.api.forum.domain.model.queries.GetAllForumRepliesFromForumPostIdQuery;
import com.agrotech.api.forum.domain.services.ForumReplyCommandService;
import com.agrotech.api.forum.domain.services.ForumReplyQueryService;
import com.agrotech.api.forum.interfaces.rest.resources.CreateForumReplyResource;
import com.agrotech.api.forum.interfaces.rest.resources.ForumReplyResource;
import com.agrotech.api.forum.interfaces.rest.transform.CreateForumReplyCommandFromResourceAssembler;
import com.agrotech.api.forum.interfaces.rest.transform.ForumReplyResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RestController
@RequestMapping(value = "api/v1/forum-replies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ForumReplies", description = "Forum Reply Management Endpoints")
public class ForumRepliesController {

    private final ForumReplyCommandService forumReplyCommandService;
    private final ForumReplyQueryService forumReplyQueryService;

    public ForumRepliesController(ForumReplyCommandService forumReplyCommandService,
                                   ForumReplyQueryService forumReplyQueryService) {
        this.forumReplyCommandService = forumReplyCommandService;
        this.forumReplyQueryService = forumReplyQueryService;
    }

    @GetMapping
    public ResponseEntity<List<ForumReplyResource>> getRepliesByPost(@RequestParam("forumPostId") Long forumPostId) {
        var query = new GetAllForumRepliesFromForumPostIdQuery(forumPostId);
        List<ForumReply> replies = forumReplyQueryService.handle(query);
        var resources = replies.stream()
                .map(ForumReplyResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<ForumReplyResource> createReply(@RequestBody CreateForumReplyResource resource) {
        CreateForumReplyCommand command = CreateForumReplyCommandFromResourceAssembler.toCommandFromResource(resource);
        Long replyId = forumReplyCommandService.handle(command);

        var query = new GetAllForumRepliesFromForumPostIdQuery(resource.forumPostId());
        var replies = forumReplyQueryService.handle(query);
        var reply = replies.stream().filter(r -> r.getId().equals(replyId)).findFirst();

        if (reply.isEmpty()) return ResponseEntity.badRequest().build();
        var replyResource = ForumReplyResourceFromEntityAssembler.toResourceFromEntity(reply.get());
        return new ResponseEntity<>(replyResource, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReply(@PathVariable Long id) {
        var command = new DeleteForumReplyCommand(id);
        forumReplyCommandService.handle(command);
        return ResponseEntity.ok("Reply with id " + id + " successfully deleted");
    }
}

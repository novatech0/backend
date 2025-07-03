package com.agrotech.api.forum.interfaces.rest;

import com.agrotech.api.forum.domain.model.commands.CreateForumFavoriteCommand;
import com.agrotech.api.forum.domain.model.commands.DeleteForumFavoriteCommand;
import com.agrotech.api.forum.domain.model.entities.ForumFavorite;
import com.agrotech.api.forum.domain.model.queries.CheckForumFavoriteExistsQuery;
import com.agrotech.api.forum.domain.model.queries.GetAllForumFavoritesByForumPostIdQuery;
import com.agrotech.api.forum.domain.model.queries.GetAllForumFavoritesByUserIdQuery;
import com.agrotech.api.forum.domain.services.ForumFavoriteCommandService;
import com.agrotech.api.forum.domain.services.ForumFavoriteQueryService;
import com.agrotech.api.forum.interfaces.rest.resources.CreateForumFavoriteResource;
import com.agrotech.api.forum.interfaces.rest.resources.ForumFavoriteResource;
import com.agrotech.api.forum.interfaces.rest.transform.CreateForumFavoriteCommandFromResourceAssembler;
import com.agrotech.api.forum.interfaces.rest.transform.ForumFavoriteResourceFromEntityAssembler;
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
@RequestMapping(value = "api/v1/forum-favorites", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ForumFavorites", description = "Forum Favorite Management Endpoints")
public class ForumFavoritesController {

    private final ForumFavoriteCommandService commandService;
    private final ForumFavoriteQueryService queryService;

    public ForumFavoritesController(ForumFavoriteCommandService commandService,
                                    ForumFavoriteQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<ForumFavoriteResource> create(@RequestBody CreateForumFavoriteResource resource) {
        CreateForumFavoriteCommand command = CreateForumFavoriteCommandFromResourceAssembler.toCommandFromResource(resource);
        Long id = commandService.handle(command);

        var optionalFavorite = queryService.handle(new CheckForumFavoriteExistsQuery(resource.userId(), resource.forumPostId()));
        if (optionalFavorite.isEmpty()) return ResponseEntity.badRequest().build();

        var favoriteResource = ForumFavoriteResourceFromEntityAssembler.toResourceFromEntity(optionalFavorite.get());
        return new ResponseEntity<>(favoriteResource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ForumFavoriteResource>> getAllByForumPost(@RequestParam(required = false) Long userId, @RequestParam(required = false) Long forumPostId) {
        List<ForumFavorite> favorites;
        if (userId != null) {
            // Get all favorites by user
            var query = new GetAllForumFavoritesByUserIdQuery(userId);
            favorites = queryService.handle(query);
        }
        else if (forumPostId != null) {
            // Get all favorites by forum post
            var query = new GetAllForumFavoritesByForumPostIdQuery(forumPostId);
            favorites = queryService.handle(query);
        }
        else {
            return ResponseEntity.badRequest().build();
        }

        var resources = favorites.stream()
                .map(ForumFavoriteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> exists(@RequestParam Long userId, @RequestParam Long forumPostId) {
        var exists = queryService.handle(new CheckForumFavoriteExistsQuery(userId, forumPostId)).isPresent();
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long userId, @RequestParam Long forumPostId) {
        commandService.handle(new DeleteForumFavoriteCommand(userId, forumPostId));
        return ResponseEntity.ok("Favorite deleted");
    }
}

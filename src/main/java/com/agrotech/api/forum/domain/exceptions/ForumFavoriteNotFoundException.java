package com.agrotech.api.forum.domain.exceptions;

public class ForumFavoriteNotFoundException extends RuntimeException {
    public ForumFavoriteNotFoundException(Long forumPostId, Long userId) {
        super("Forum Favorite with forumPostId " + forumPostId + " and userId " + userId + " not found");
    }
}

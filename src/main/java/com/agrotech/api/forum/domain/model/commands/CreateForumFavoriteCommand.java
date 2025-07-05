package com.agrotech.api.forum.domain.model.commands;

public record CreateForumFavoriteCommand(Long userId, Long forumPostId) {
}

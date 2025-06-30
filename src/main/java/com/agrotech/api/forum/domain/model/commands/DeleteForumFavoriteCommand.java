package com.agrotech.api.forum.domain.model.commands;

public record DeleteForumFavoriteCommand(Long userId, Long forumPostId) {
}

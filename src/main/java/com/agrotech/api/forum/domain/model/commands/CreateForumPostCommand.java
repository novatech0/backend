package com.agrotech.api.forum.domain.model.commands;

public record CreateForumPostCommand(Long userId, String title, String content) {
}

package com.agrotech.api.forum.domain.model.commands;

public record CreateForumReplyCommand(Long userId, Long forumPostId, String content) {

}

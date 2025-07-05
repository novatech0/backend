package com.agrotech.api.forum.interfaces.rest.resources;

public record ForumReplyResource(Long id, Long userId, Long forumPostId, String content) {
}

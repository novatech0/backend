package com.agrotech.api.forum.interfaces.rest.resources;

public record CreateForumReplyResource(Long userId, Long forumPostId, String content) {
}

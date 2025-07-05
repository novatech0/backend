package com.agrotech.api.forum.interfaces.rest.resources;

public record ForumPostResource(Long id, Long userId, String title, String content) {
}

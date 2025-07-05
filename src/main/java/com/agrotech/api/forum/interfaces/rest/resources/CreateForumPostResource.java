package com.agrotech.api.forum.interfaces.rest.resources;

public record CreateForumPostResource(Long userId, String title, String content) {
}

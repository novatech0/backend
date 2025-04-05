package com.agrotech.api.post.interfaces.rest.resources;

public record CreatePostResource(Long advisorId, String title, String description, String image) {
}

package com.agrotech.api.forum.domain.exceptions;

public class ForumPostNotFoundException extends RuntimeException {
    public ForumPostNotFoundException(Long id) {
        super("Forum Post with id " + id + " not found");
    }
}

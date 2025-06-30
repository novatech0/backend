package com.agrotech.api.forum.domain.exceptions;

public class ForumReplyNotFoundException extends RuntimeException {
    public ForumReplyNotFoundException(Long id) {
        super("Forum Reply with id " + id + " not found");
    }
}

package com.agrotech.api.profile.domain.exceptions;

public class SameUserException extends RuntimeException {
    public SameUserException(Long userId) {
        super("Profile with user ID " + userId + " already exists");
    }
}

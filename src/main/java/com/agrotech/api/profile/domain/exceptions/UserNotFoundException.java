package com.agrotech.api.profile.domain.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " not found");
    }
}

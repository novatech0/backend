package com.agrotech.api.post.domain.exceptions;

public class AdvisorNotFoundException extends RuntimeException {
    public AdvisorNotFoundException(Long id) {
        super("Advisor with id " + id + " not found");
    }
}

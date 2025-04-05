package com.agrotech.api.profile.domain.exceptions;

public class FarmerNotFoundException extends RuntimeException {
    public FarmerNotFoundException(Long id) {
        super("Farmer with id " + id + " not found");
    }
}

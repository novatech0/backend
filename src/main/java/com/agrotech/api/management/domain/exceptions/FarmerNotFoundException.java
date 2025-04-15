package com.agrotech.api.management.domain.exceptions;

public class FarmerNotFoundException extends RuntimeException {
    public FarmerNotFoundException(Long farmerId) {
        super("Farmer with id " + farmerId + " not found");
    }
}


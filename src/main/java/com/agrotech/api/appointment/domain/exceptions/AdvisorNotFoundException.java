package com.agrotech.api.appointment.domain.exceptions;

public class AdvisorNotFoundException extends RuntimeException {
    public AdvisorNotFoundException(Long advisorId) {
        super("Advisor with id " + advisorId + " not found");
    }
}

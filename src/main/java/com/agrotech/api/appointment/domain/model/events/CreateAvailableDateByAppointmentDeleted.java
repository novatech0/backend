package com.agrotech.api.appointment.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class CreateAvailableDateByAppointmentDeleted extends ApplicationEvent {
    private final Long advisorId;
    private final Long farmerId;
    private final LocalDate availableDate;
    private final String startTime;
    private final String endTime;

    public CreateAvailableDateByAppointmentDeleted(Object source, Long advisorId, Long farmerId, LocalDate availableDate, String startTime, String endTime) {
        super(source);
        this.advisorId = advisorId;
        this.farmerId = farmerId;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}

package com.agrotech.api.appointment.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class DeleteAvailableDateByAppointmentCreated extends ApplicationEvent {
    private final Long advisorId;
    private LocalDate scheduledDate;
    private String startTime;
    private String endTime;

    public DeleteAvailableDateByAppointmentCreated(Object source, Long advisorId, LocalDate scheduledDate, String startTime, String endTime) {
        super(source);
        this.advisorId = advisorId;
        this.scheduledDate = scheduledDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

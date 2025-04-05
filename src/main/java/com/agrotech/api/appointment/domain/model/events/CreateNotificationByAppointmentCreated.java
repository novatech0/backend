package com.agrotech.api.appointment.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateNotificationByAppointmentCreated extends ApplicationEvent {

    private final Long farmerId;
    private final Long advisorId;

    public CreateNotificationByAppointmentCreated(Object source, Long farmerId, Long advisorId) {
        super(source);
        this.farmerId = farmerId;
        this.advisorId = advisorId;
    }

}


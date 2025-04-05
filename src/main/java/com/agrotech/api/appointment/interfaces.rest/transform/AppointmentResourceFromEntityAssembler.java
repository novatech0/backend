package com.agrotech.api.appointment.interfaces.rest.transform;

import com.agrotech.api.appointment.domain.model.aggregates.Appointment;
import com.agrotech.api.appointment.interfaces.rest.resources.AppointmentResource;

public class AppointmentResourceFromEntityAssembler {
    public static AppointmentResource toResourceFromEntity(Appointment entity){
        return new AppointmentResource(
                entity.getId(),
                entity.getAdvisorId(),
                entity.getFarmerId(),
                entity.getMessage(),
                entity.getAppointmentStatus(),
                entity.getScheduledDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getMeetingUrl()
        );
    }
}

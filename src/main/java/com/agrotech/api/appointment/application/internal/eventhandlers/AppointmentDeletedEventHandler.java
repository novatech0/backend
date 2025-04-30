package com.agrotech.api.appointment.application.internal.eventhandlers;

import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalNotificationsService;
import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalProfilesService;
import com.agrotech.api.appointment.domain.model.commands.CreateAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.events.CreateAvailableDateByAppointmentDeleted;
import com.agrotech.api.appointment.domain.services.AvailableDateCommandService;
import com.agrotech.api.shared.domain.exceptions.AdvisorNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppointmentDeletedEventHandler {
    private final AvailableDateCommandService availableDateCommandService;
    private final ExternalNotificationsService externalNotificationsService;
    private final ExternalProfilesService externalProfilesService;

    public AppointmentDeletedEventHandler(AvailableDateCommandService availableDateCommandService,
                                          ExternalNotificationsService externalNotificationsService,
                                          ExternalProfilesService externalProfilesService) {
        this.availableDateCommandService = availableDateCommandService;
        this.externalNotificationsService = externalNotificationsService;
        this.externalProfilesService = externalProfilesService;
    }

    @EventListener
    public void onAppointmentDeleted(CreateAvailableDateByAppointmentDeleted event) {
        availableDateCommandService.handle(new CreateAvailableDateCommand(
                event.getAdvisorId(),
                event.getAvailableDate(),
                event.getStartTime(),
                event.getEndTime()
        ));

        var advisor = externalProfilesService.fetchAdvisorById(event.getAdvisorId()).orElseThrow(
                () -> new AdvisorNotFoundException(event.getAdvisorId())
        );

        externalNotificationsService.createNotification(advisor.getUserId(), "Cita Cancelada",
                "Se ha cancelado una cita programada con un agricultor para el dia " + event.getAvailableDate() + " a las " + event.getStartTime(),
                new Date());
    }
}

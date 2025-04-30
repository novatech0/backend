package com.agrotech.api.appointment.application.internal.eventhandlers;

import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalNotificationsService;
import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalProfilesService;
import com.agrotech.api.appointment.domain.exceptions.AvailableDateNotFoundException;
import com.agrotech.api.appointment.domain.model.commands.DeleteAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.events.CreateNotificationByAppointmentCreated;
import com.agrotech.api.appointment.domain.model.events.DeleteAvailableDateByAppointmentCreated;
import com.agrotech.api.appointment.domain.model.queries.GetAvailableDateByAdvisorIdAndDate;
import com.agrotech.api.appointment.domain.services.AvailableDateCommandService;
import com.agrotech.api.appointment.domain.services.AvailableDateQueryService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class AppointmentCreatedEventHandler {
    private final ExternalProfilesService externalProfilesService;
    private final ExternalNotificationsService externalNotificationsService;
    private final AvailableDateCommandService availableDateCommandService;
    private final AvailableDateQueryService availableDateQueryService;

    public AppointmentCreatedEventHandler(ExternalProfilesService externalProfileService,
                                          ExternalNotificationsService externalNotificationsService,
                                          AvailableDateCommandService availableDateCommandService,
                                          AvailableDateQueryService availableDateQueryService) {
        this.externalProfilesService = externalProfileService;
        this.externalNotificationsService = externalNotificationsService;
        this.availableDateCommandService = availableDateCommandService;
        this.availableDateQueryService = availableDateQueryService;
    }

    @EventListener
    public void onAppointmentCreatedCreateNotification(CreateNotificationByAppointmentCreated event) {
        Date date = new Date();

        var farmer = externalProfilesService.fetchFarmerById(event.getFarmerId()).orElseThrow();
        var advisor = externalProfilesService.fetchAdvisorById(event.getAdvisorId()).orElseThrow();
        var profileFarmer = externalProfilesService.fetchProfileByFarmerId(event.getFarmerId()).orElseThrow();
        var profileAdvisor = externalProfilesService.fetchProfileByAdvisorId(event.getAdvisorId()).orElseThrow();

        var meetingUrl = "https://meet.jit.si/agrotechMeeting" + event.getFarmerId() + "-" + event.getAdvisorId();
        externalNotificationsService.createNotification(farmer.getUserId(), "Proximo Asesoramiento",
                "Tienes un asesoramiento programado con " + profileAdvisor.getFirstName() + " " + profileAdvisor.getLastName(),
                date);
        externalNotificationsService.createNotification(advisor.getUserId(), "Proximo Asesoramiento",
                "Tienes una asesoria programada con " + profileFarmer.getFirstName() + " " + profileFarmer.getLastName(),
                date);
    }

    @EventListener
    public void onAppointmentCreatedDeleteAvailableDate(DeleteAvailableDateByAppointmentCreated event) {
        var query = new GetAvailableDateByAdvisorIdAndDate(event.getAdvisorId(), event.getScheduledDate(), event.getStartTime(), event.getEndTime()
        );
        var availableDate = availableDateQueryService.handle(query).orElseThrow(AvailableDateNotFoundException::new);
        availableDateCommandService.handle(new DeleteAvailableDateCommand(availableDate.getId()));
    }
}
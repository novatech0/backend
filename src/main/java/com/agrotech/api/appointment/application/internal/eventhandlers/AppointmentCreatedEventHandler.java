package com.agrotech.api.appointment.application.internal.eventhandlers;

import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalNotificationsService;
import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalProfilesService;
import com.agrotech.api.appointment.domain.model.events.CreateNotificationByAppointmentCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class AppointmentCreatedEventHandler {
    private final ExternalProfilesService externalProfilesService;
    private final ExternalNotificationsService externalNotificationsService;
    public AppointmentCreatedEventHandler(ExternalProfilesService externalProfileService, ExternalNotificationsService externalNotificationsService) {
        this.externalProfilesService = externalProfileService;
        this.externalNotificationsService = externalNotificationsService;
    }

    @EventListener
    public void onAppointmentCreated(CreateNotificationByAppointmentCreated event) {
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

}
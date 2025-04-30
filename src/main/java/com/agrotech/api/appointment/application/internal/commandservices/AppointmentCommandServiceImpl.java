package com.agrotech.api.appointment.application.internal.commandservices;

import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalProfilesService;
import com.agrotech.api.appointment.domain.exceptions.*;
import com.agrotech.api.appointment.domain.model.events.CreateAvailableDateByAppointmentDeleted;
import com.agrotech.api.appointment.domain.model.events.CreateNotificationByAppointmentCreated;
import com.agrotech.api.appointment.domain.model.events.DeleteAvailableDateByAppointmentCreated;
import com.agrotech.api.shared.domain.exceptions.*;
import com.agrotech.api.appointment.domain.model.aggregates.Appointment;
import com.agrotech.api.appointment.domain.model.commands.CreateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.commands.DeleteAppointmentCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateAppointmentCommand;
import com.agrotech.api.appointment.domain.model.valueobjects.AppointmentStatus;
import com.agrotech.api.appointment.domain.services.AppointmentCommandService;
import com.agrotech.api.appointment.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {
    private final AppointmentRepository appointmentRepository;
    private final ExternalProfilesService externalProfilesService;
    private final ApplicationEventPublisher eventPublisher;

    public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository,
                                         ExternalProfilesService externalProfilesService,
                                         ApplicationEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.externalProfilesService = externalProfilesService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Long handle(CreateAppointmentCommand command) {
        var advisor = externalProfilesService.fetchAdvisorById(command.advisorId());
        if (advisor.isEmpty()) throw new AdvisorNotFoundException(command.advisorId());
        var farmer = externalProfilesService.fetchFarmerById(command.farmerId());
        if (farmer.isEmpty()) throw new FarmerNotFoundException(command.farmerId());

        // Verification of the date
        if (command.scheduledDate().isBefore(LocalDateTime.now().toLocalDate())) {
            throw new InvalidDateException(command.scheduledDate());
        }

        // Verification of Status
        if (command.status() != null && !command.status().matches("^(?i)(PENDING|ONGOING|COMPLETED)$")) {
            throw new InvalidStatusException(command.status());
        }
        //Verification Start time and End time are in the format HH:mm
        if (!command.startTime().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") || !command.endTime().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            throw new IncorrectTimeFormatException(command.startTime(), command.endTime());
        }

        var meetingUrl = "https://meet.jit.si/agrotechMeeting" + command.farmerId() + "-" + command.advisorId();

        Appointment appointment = new Appointment(command, meetingUrl, advisor.get(), farmer.get());
        appointmentRepository.save(appointment);
        eventPublisher.publishEvent(new CreateNotificationByAppointmentCreated(this, command.farmerId(), command.advisorId()));
        eventPublisher.publishEvent(new DeleteAvailableDateByAppointmentCreated(this, command.advisorId(), command.scheduledDate(), command.startTime(), command.endTime()));
        return appointment.getId();
    }

    @Override
    public Optional<Appointment> handle(UpdateAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.id());
        if (appointment.isEmpty()) return Optional.empty();
        // Verification of Status
        if (command.status() != null && !command.status().matches("^(?i)(PENDING|ONGOING|COMPLETED)$")) {
            throw new InvalidStatusException(command.status());
        }
        //Verification Start time and End time are in the format HH:mm
        if (!command.startTime().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") || !command.endTime().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            throw new IncorrectTimeFormatException(command.startTime(), command.endTime());
        }
        var appointmentToUpdate = appointment.get();
        Appointment updatedAppointment = appointmentRepository.save(appointmentToUpdate.update(command));
        return Optional.of(updatedAppointment);
    }

    @Override
    @Transactional
    public void handle(DeleteAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.id());
        if (appointment.isEmpty()) throw new AdvisorNotFoundException(command.id());
        eventPublisher.publishEvent(new CreateAvailableDateByAppointmentDeleted(this, appointment.get().getAdvisorId(), appointment.get().getFarmerId(), appointment.get().getScheduledDate(), appointment.get().getStartTime(), appointment.get().getEndTime()));
        appointmentRepository.delete(appointment.get());
    }

    public void updateAppointmentStatuses(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            updateAppointmentStatus(appointment);
        }
    }

    public void updateAppointmentStatus(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(appointment.getScheduledDate(), LocalTime.parse(appointment.getStartTime()));
        LocalDateTime end = LocalDateTime.of(appointment.getScheduledDate(), LocalTime.parse(appointment.getEndTime()));

        // Determine the new status
        String newStatus;
        if (now.isAfter(end)) {
            newStatus = AppointmentStatus.COMPLETED.name();
        } else if (now.isAfter(start) && now.isBefore(end)) {
            newStatus = AppointmentStatus.ONGOING.name();
        } else {
            newStatus = appointment.getAppointmentStatus();
        }

        // Update the status if it has changed
        if (!appointment.getAppointmentStatus().equals(newStatus)) {
            var updateCommand = new UpdateAppointmentCommand(
                    appointment.getId(),
                    appointment.getMessage(),
                    newStatus,
                    appointment.getScheduledDate(),
                    appointment.getStartTime(),
                    appointment.getEndTime()
            );

            appointment.update(updateCommand);
            appointmentRepository.save(appointment);
        }
    }
}

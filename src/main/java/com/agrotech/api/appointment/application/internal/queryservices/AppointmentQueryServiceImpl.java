package com.agrotech.api.appointment.application.internal.queryservices;

import com.agrotech.api.appointment.application.internal.commandservices.AppointmentCommandServiceImpl;
import com.agrotech.api.appointment.domain.model.aggregates.Appointment;
import com.agrotech.api.appointment.domain.model.queries.*;
import com.agrotech.api.appointment.domain.services.AppointmentQueryService;
import com.agrotech.api.appointment.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentCommandServiceImpl appointmentCommandService;

    public AppointmentQueryServiceImpl(AppointmentRepository appointmentRepository, AppointmentCommandServiceImpl appointmentCommandService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentCommandService = appointmentCommandService;
    }

    @Override
    public List<Appointment> handle(GetAllAppointmentsQuery query) {
        List<Appointment> appointments = this.appointmentRepository.findAll();
        appointmentCommandService.updateAppointmentStatuses(appointments);
        return appointments;
    }

    @Override
    public Optional<Appointment> handle(GetAppointmentByIdQuery query) {
        Optional<Appointment> appointment = this.appointmentRepository.findById(query.id());
        appointment.ifPresent(appointmentCommandService::updateAppointmentStatus);
        return appointment;
    }

    @Override
    public List<Appointment> handle(GetAppointmentsByFarmerIdQuery query) {
        List<Appointment> appointments = this.appointmentRepository.findByFarmer_Id(query.farmerId());
        appointmentCommandService.updateAppointmentStatuses(appointments);
        return appointments;
    }

    @Override
    public List<Appointment> handle(GetAppointmentsByAdvisorIdQuery query) {
        List<Appointment> appointments = this.appointmentRepository.findByAdvisor_Id(query.advisorId());
        appointmentCommandService.updateAppointmentStatuses(appointments);
        return appointments;
    }

    @Override
    public List<Appointment> handle(GetAppointmentsByAdvisorIdAndFarmerIdQuery query) {
        List<Appointment> appointments = this.appointmentRepository.findByAdvisor_IdAndFarmer_Id(query.advisorId(), query.farmerId());
        appointmentCommandService.updateAppointmentStatuses(appointments);
        return appointments;
    }
}
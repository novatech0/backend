package com.agrotech.api.appointment.application.internal.commandservices;

import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalProfilesService;
import com.agrotech.api.appointment.domain.exceptions.AvailableDateNotFoundException;
import com.agrotech.api.appointment.domain.exceptions.IncorrectTimeFormatException;
import com.agrotech.api.appointment.domain.model.commands.CreateAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.commands.DeleteAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.commands.UpdateAvailableDateCommand;
import com.agrotech.api.appointment.domain.model.entities.AvailableDate;
import com.agrotech.api.appointment.domain.services.AvailableDateCommandService;
import com.agrotech.api.appointment.infrastructure.persistence.jpa.repositories.AvailableDateRepository;
import com.agrotech.api.shared.domain.exceptions.AdvisorNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AvailableDateCommandServiceImpl implements AvailableDateCommandService {
    private final AvailableDateRepository availableDateRepository;
    private final ExternalProfilesService externalProfilesService;

    public AvailableDateCommandServiceImpl(AvailableDateRepository availableDateRepository, ExternalProfilesService externalProfilesService) {
        this.availableDateRepository = availableDateRepository;
        this.externalProfilesService = externalProfilesService;
    }

    @Override
    public Long handle(CreateAvailableDateCommand command) {
        var advisor = externalProfilesService.fetchAdvisorById(command.advisorId());
        if(advisor.isEmpty()) throw new AdvisorNotFoundException(command.advisorId());
        if (!command.startTime().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") || !command.endTime().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            throw new IncorrectTimeFormatException(command.startTime(), command.endTime());
        }
        var availableDate = new AvailableDate(command, advisor.get());
        availableDateRepository.save(availableDate);
        return availableDate.getId();
    }

    @Override
    public Optional<AvailableDate> handle(UpdateAvailableDateCommand command) {
        var availableDate = availableDateRepository.findById(command.id());
        if(availableDate.isEmpty()) return Optional.empty();
        if (!command.startTime().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") || !command.endTime().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            throw new IncorrectTimeFormatException(command.startTime(), command.endTime());
        }
        var availableDateToUpdate = availableDate.get();
        availableDateRepository.save(availableDateToUpdate.update(command));
        return Optional.of(availableDateToUpdate);
    }

    @Override
    public void handle(DeleteAvailableDateCommand command) {
        var availableDate = availableDateRepository.findById(command.id());
        if(availableDate.isEmpty()) throw new AvailableDateNotFoundException(command.id());
        availableDateRepository.delete(availableDate.get());
    }
}

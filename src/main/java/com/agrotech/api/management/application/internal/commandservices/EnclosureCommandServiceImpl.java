package com.agrotech.api.management.application.internal.commandservices;

import com.agrotech.api.management.domain.exceptions.EnclosureNotFoundException;
import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import com.agrotech.api.management.domain.model.commands.CreateEnclosureCommand;
import com.agrotech.api.management.domain.model.commands.DeleteEnclosureCommand;
import com.agrotech.api.management.domain.model.commands.UpdateEnclosureCommand;
import com.agrotech.api.management.domain.services.EnclosureCommandService;
import com.agrotech.api.management.infrastructure.persitence.jpa.repositories.EnclosureRepository;
import com.agrotech.api.profile.infrastructure.persistence.jpa.repositories.FarmerRepository;
import com.agrotech.api.shared.domain.exceptions.FarmerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnclosureCommandServiceImpl implements EnclosureCommandService {
    private final EnclosureRepository enclosureRepository;
    private final FarmerRepository farmerRepository;

    public EnclosureCommandServiceImpl(EnclosureRepository enclosureRepository, FarmerRepository farmerRepository) {
        this.enclosureRepository = enclosureRepository;
        this.farmerRepository = farmerRepository;
    }

    @Override
    public Long handle(CreateEnclosureCommand command) {
        var farmer = farmerRepository.findById(command.farmerId());
        if (farmer.isEmpty()) throw new FarmerNotFoundException(command.farmerId());

        Enclosure enclosure = new Enclosure(command, farmer.get());
        enclosureRepository.save(enclosure);
        return enclosure.getId();
    }

    @Override
    public Optional<Enclosure> handle(UpdateEnclosureCommand command) {
        var enclosure = enclosureRepository.findById(command.enclosureId());
        if (enclosure.isEmpty()) throw new EnclosureNotFoundException(command.enclosureId());

        var enclosureToUpdate = enclosure.get();
        Enclosure updatedEnclosure = enclosureRepository.save(enclosureToUpdate.update(command));
        return Optional.of(updatedEnclosure);
    }

    @Override
    public void handle(DeleteEnclosureCommand command) {
        var enclosure = enclosureRepository.findById(command.enclosureId());
        if (enclosure.isEmpty()) throw new EnclosureNotFoundException(command.enclosureId());
        enclosureRepository.delete(enclosure.get());
    }
}

package com.agrotech.api.management.application.internal.commandservices;

import com.agrotech.api.management.domain.exceptions.AnimalNotFoundException;
import com.agrotech.api.management.domain.exceptions.EnclosureNotFoundException;
import com.agrotech.api.management.domain.exceptions.IncorrectHealthStatusException;
import com.agrotech.api.management.domain.model.commands.CreateAnimalCommand;
import com.agrotech.api.management.domain.model.commands.DeleteAnimalCommand;
import com.agrotech.api.management.domain.model.commands.UpdateAnimalCommand;
import com.agrotech.api.management.domain.model.entities.Animal;
import com.agrotech.api.management.domain.services.AnimalCommandService;
import com.agrotech.api.management.infrastructure.persistence.jpa.mappers.AnimalMapper;
import com.agrotech.api.management.infrastructure.persistence.jpa.mappers.EnclosureMapper;
import com.agrotech.api.management.infrastructure.persistence.jpa.repositories.AnimalRepository;
import com.agrotech.api.management.infrastructure.persistence.jpa.repositories.EnclosureRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnimalCommandServiceImpl implements AnimalCommandService {
    private final AnimalRepository animalRepository;
    private final EnclosureRepository enclosureRepository;

    public AnimalCommandServiceImpl(AnimalRepository animalRepository, EnclosureRepository enclosureRepository) {
        this.animalRepository = animalRepository;
        this.enclosureRepository = enclosureRepository;
    }

    @Override
    public Long handle(CreateAnimalCommand command) {
        var enclosure = enclosureRepository.findById(command.enclosureId());
        if (enclosure.isEmpty()) throw new EnclosureNotFoundException(command.enclosureId());
        var animal = new Animal(command, EnclosureMapper.toDomain(enclosure.get()));
        animalRepository.save(AnimalMapper.toEntity(animal));
        return animal.getId();
    }

    @Override
    public Optional<Animal> handle(UpdateAnimalCommand command) {
        var animalEntity = animalRepository.findById(command.animalId());
        if (animalEntity.isEmpty()) return Optional.empty();
        if (command.health() != null  &&  !command.health().matches("^(?i)(HEALTHY|SICK|DEAD|UNKNOWN)$")) {
            throw new IncorrectHealthStatusException(command.health());
        }
        var animal = AnimalMapper.toDomain(animalEntity.get());
        animalRepository.save(AnimalMapper.toEntity(animal.update(command)));
        return Optional.of(animal);
    }

    @Override
    public void handle(DeleteAnimalCommand command) {
        var animal = animalRepository.findById(command.animalId());
        if (animal.isEmpty()) throw new AnimalNotFoundException(command.animalId());
        animalRepository.delete(animal.get());
    }
}

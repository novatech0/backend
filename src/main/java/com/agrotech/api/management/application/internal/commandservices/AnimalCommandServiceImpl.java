package com.agrotech.api.management.application.internal.commandservices;

import com.agrotech.api.management.domain.exceptions.AnimalNotFoundException;
import com.agrotech.api.management.domain.exceptions.EnclosureNotFoundException;
import com.agrotech.api.management.domain.exceptions.IncorrectHealthStatusException;
import com.agrotech.api.management.domain.model.commands.CreateAnimalCommand;
import com.agrotech.api.management.domain.model.commands.DeleteAnimalCommand;
import com.agrotech.api.management.domain.model.commands.UpdateAnimalCommand;
import com.agrotech.api.management.domain.model.entities.Animal;
import com.agrotech.api.management.domain.services.AnimalCommandService;
import com.agrotech.api.management.infrastructure.persitence.jpa.repositories.AnimalRepository;
import com.agrotech.api.management.infrastructure.persitence.jpa.repositories.EnclosureRepository;
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

        Animal animal = new Animal(command, enclosure.get());
        animalRepository.save(animal);
        return animal.getId();
    }

    @Override
    public Optional<Animal> handle(UpdateAnimalCommand command) {
        var animal = animalRepository.findById(command.animalId());
        if (animal.isEmpty()) return Optional.empty();
        if (command.health() != null  &&  !command.health().matches("^(?i)(HEALTHY|SICK|DEAD|UNKNOWN)$")) {
            throw new IncorrectHealthStatusException(command.health());
        }

        var animalToUpdate = animal.get();
        Animal updatedAnimal = animalRepository.save(animalToUpdate.update(command));
        return Optional.of(updatedAnimal);
    }

    @Override
    public void handle(DeleteAnimalCommand command) {
        var animal = animalRepository.findById(command.animalId());
        if (animal.isEmpty()) throw new AnimalNotFoundException(command.animalId());
        animalRepository.delete(animal.get());
    }
}

package com.agrotech.api.management.application.internal.queryservices;

import com.agrotech.api.management.domain.model.entities.Animal;
import com.agrotech.api.management.domain.model.queries.GetAllAnimalsByEnclosureIdQuery;
import com.agrotech.api.management.domain.model.queries.GetAllAnimalsQuery;
import com.agrotech.api.management.domain.model.queries.GetAnimalByIdQuery;
import com.agrotech.api.management.domain.services.AnimalQueryService;
import com.agrotech.api.management.infrastructure.persitence.jpa.repositories.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalQueryServiceImpl implements AnimalQueryService {
    private final AnimalRepository animalRepository;

    public AnimalQueryServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public List<Animal> handle(GetAllAnimalsQuery query) {
        return this.animalRepository.findAll();
    }

    @Override
    public Optional<Animal> handle(GetAnimalByIdQuery query) {
        return  this.animalRepository.findById(query.animalId());
    }

    @Override
    public List<Animal> handle(GetAllAnimalsByEnclosureIdQuery query) {
        return this.animalRepository.findAllByEnclosure_Id(query.enclosureId());
    }
}

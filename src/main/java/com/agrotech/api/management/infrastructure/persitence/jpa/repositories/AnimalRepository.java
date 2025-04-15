package com.agrotech.api.management.infrastructure.persitence.jpa.repositories;

import com.agrotech.api.management.domain.model.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findAllByEnclosure_Id(Long enclosureId);
}

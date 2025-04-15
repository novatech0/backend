package com.agrotech.api.management.infrastructure.persitence.jpa.repositories;

import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnclosureRepository extends JpaRepository<Enclosure, Long> {
    List<Enclosure> findAllByFarmer_Id(Long farmerId);
}

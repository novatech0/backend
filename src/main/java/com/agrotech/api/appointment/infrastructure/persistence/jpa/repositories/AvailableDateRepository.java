package com.agrotech.api.appointment.infrastructure.persistence.jpa.repositories;

import com.agrotech.api.appointment.domain.model.entities.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailableDateRepository extends JpaRepository<AvailableDate, Long> {
    List<AvailableDate> findByAdvisor_Id(Long advisorId);
}

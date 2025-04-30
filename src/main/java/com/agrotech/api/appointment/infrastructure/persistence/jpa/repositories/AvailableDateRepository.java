package com.agrotech.api.appointment.infrastructure.persistence.jpa.repositories;

import com.agrotech.api.appointment.domain.model.entities.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AvailableDateRepository extends JpaRepository<AvailableDate, Long> {
    List<AvailableDate> findByAdvisor_Id(Long advisorId);
    Optional<AvailableDate> findByAdvisor_IdAndAvailableDateAndStartTimeAndEndTime(Long advisorId, LocalDate availableDate, String startTime, String endTime);
}

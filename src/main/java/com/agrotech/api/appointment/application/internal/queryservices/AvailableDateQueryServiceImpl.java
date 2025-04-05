package com.agrotech.api.appointment.application.internal.queryservices;

import com.agrotech.api.appointment.domain.model.entities.AvailableDate;
import com.agrotech.api.appointment.domain.model.queries.GetAllAvailableDatesQuery;
import com.agrotech.api.appointment.domain.model.queries.GetAvailableDateByIdQuery;
import com.agrotech.api.appointment.domain.model.queries.GetAvailableDatesByAdvisorIdQuery;
import com.agrotech.api.appointment.domain.services.AvailableDateQueryService;
import com.agrotech.api.appointment.infrastructure.persistence.jpa.repositories.AvailableDateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AvailableDateQueryServiceImpl implements AvailableDateQueryService {
    private final AvailableDateRepository availableDateRepository;

    public AvailableDateQueryServiceImpl(AvailableDateRepository availableDateRepository) {
        this.availableDateRepository = availableDateRepository;
    }

    @Override
    public List<AvailableDate> handle(GetAllAvailableDatesQuery query) {
        List<AvailableDate> availableDates = availableDateRepository.findAll();
        removePastAvailableDates(availableDates);
        return availableDates;
    }

    @Override
    public Optional<AvailableDate> handle(GetAvailableDateByIdQuery query) {
        Optional<AvailableDate> availableDate = availableDateRepository.findById(query.id());
        availableDate.ifPresent(this::removePastAvailableDate);
        return availableDate;
    }

    @Override
    public List<AvailableDate> handle(GetAvailableDatesByAdvisorIdQuery query) {
        List<AvailableDate> availableDates = availableDateRepository.findByAdvisor_Id(query.advisorId());
        removePastAvailableDates(availableDates);
        return availableDates;
    }

    private void removePastAvailableDates(List<AvailableDate> availableDates) {
        for (AvailableDate availableDate : availableDates) {
            removePastAvailableDate(availableDate);
        }
    }

    private void removePastAvailableDate(AvailableDate availableDate) {
        if (availableDate.getAvailableDate().isBefore(LocalDate.now())) {
            availableDateRepository.delete(availableDate);
        }
    }
}

package com.agrotech.api.profile.application.internal.queryservices;

import com.agrotech.api.profile.domain.model.entities.Advisor;
import com.agrotech.api.profile.domain.model.queries.GetAdvisorByIdQuery;
import com.agrotech.api.profile.domain.model.queries.GetAdvisorByUserIdQuery;
import com.agrotech.api.profile.domain.model.queries.GetAllAdvisorsQuery;
import com.agrotech.api.profile.domain.services.AdvisorQueryService;
import com.agrotech.api.profile.infrastructure.persistence.jpa.repositories.AdvisorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdvisorQueryServiceImpl implements AdvisorQueryService {
    private final AdvisorRepository advisorRepository;

    public AdvisorQueryServiceImpl(AdvisorRepository advisorRepository) {
        this.advisorRepository = advisorRepository;
    }

    @Override
    public Optional<Advisor> handle(GetAdvisorByIdQuery query) {
        return advisorRepository.findById(query.id());
    }

    @Override
    public List<Advisor> handle(GetAllAdvisorsQuery query) {
        return advisorRepository.findAll();
    }

    @Override
    public Optional<Advisor> handle(GetAdvisorByUserIdQuery query) {
        return advisorRepository.findByUser_Id(query.userId());
    }
}

package com.agrotech.api.profile.application.internal.commandservices;

import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.profile.domain.exceptions.AdvisorNotFoundException;
import com.agrotech.api.profile.domain.exceptions.SameUserException;
import com.agrotech.api.profile.domain.model.commands.CreateAdvisorCommand;
import com.agrotech.api.profile.domain.model.commands.DeleteAdvisorCommand;
import com.agrotech.api.profile.domain.model.commands.UpdateAdvisorCommand;
import com.agrotech.api.profile.domain.model.entities.Advisor;
import com.agrotech.api.profile.domain.services.AdvisorCommandService;
import com.agrotech.api.profile.infrastructure.persistence.jpa.repositories.AdvisorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdvisorCommandServiceImpl implements AdvisorCommandService {
    private final AdvisorRepository advisorRepository;

    public AdvisorCommandServiceImpl(AdvisorRepository advisorRepository) {
        this.advisorRepository = advisorRepository;
    }

    @Override
    public Long handle(CreateAdvisorCommand command, User user) {
        var sameUser = advisorRepository.findByUser_Id(command.userId());
        if (sameUser.isPresent()) {
            throw new SameUserException(command.userId());
        }
        Advisor advisor = new Advisor(user);
        advisorRepository.save(advisor);
        return advisor.getId();
    }

    @Override
    public Optional<Advisor> handle(UpdateAdvisorCommand command) {
        var advisor = advisorRepository.findById(command.id());
        if (advisor.isEmpty()) {
            return Optional.empty();
        }
        var advisorToUpdate = advisor.get();
        Advisor updatedAdvisor = advisorRepository.save(advisorToUpdate.update(command));
        return Optional.of(updatedAdvisor);
    }

    @Override
    public void handle(DeleteAdvisorCommand command) {
        var advisor = advisorRepository.findById(command.id());
        if (advisor.isEmpty()) {
            throw new AdvisorNotFoundException(command.id());
        }
        advisorRepository.delete(advisor.get());
    }
}

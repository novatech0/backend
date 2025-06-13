package com.agrotech.api.profile.application.internal.commandservices;

import com.agrotech.api.profile.application.internal.outboundservices.acl.ExternalUserService;
import com.agrotech.api.profile.domain.exceptions.ProfileNotFoundException;
import com.agrotech.api.profile.domain.exceptions.UserAlreadyUsedException;
import com.agrotech.api.profile.infrastructure.persistence.jpa.mappers.ProfileMapper;
import com.agrotech.api.shared.domain.exceptions.UserNotFoundException;
import com.agrotech.api.profile.domain.model.aggregates.Profile;
import com.agrotech.api.profile.domain.model.commands.CreateProfileCommand;
import com.agrotech.api.profile.domain.model.commands.DeleteProfileCommand;
import com.agrotech.api.profile.domain.model.commands.UpdateProfileCommand;
import com.agrotech.api.profile.domain.services.ProfileCommandService;
import com.agrotech.api.profile.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {
    private final ProfileRepository profileRepository;
    private final ExternalUserService externalUserService;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository, ExternalUserService externalUserService) {
        this.profileRepository = profileRepository;
        this.externalUserService = externalUserService;
    }

    @Override
    public Long handle(CreateProfileCommand command) {
        var user = externalUserService.fetchUserById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
       profileRepository.findByUser_Id(command.userId())
                .ifPresent(existingProfile -> {
                    throw new UserAlreadyUsedException(command.userId());
                });
        var profile = new Profile(command, user);
        var profileEntity = profileRepository.save(ProfileMapper.toEntity(profile));
        return profileEntity.getId();
    }

    @Override
    public Optional<Profile> handle(UpdateProfileCommand command) {
        var profileEntity = profileRepository.findById(command.id())
                .orElseThrow(() -> new ProfileNotFoundException(command.id()));
        profileEntity.update(command);
        var updatedEntity = profileRepository.save(profileEntity);
        return Optional.of(ProfileMapper.toDomain(updatedEntity));
    }

    @Override
    public void handle(DeleteProfileCommand command) {
        var profileEntity = profileRepository.findById(command.id())
                .orElseThrow(() -> new ProfileNotFoundException(command.id()));
        profileRepository.delete(profileEntity);
    }
}
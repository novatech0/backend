package com.agrotech.api.iam.application.internal.commandservices;

import com.agrotech.api.iam.application.internal.outboundservices.acl.ExternalProfileRoleService;
import com.agrotech.api.iam.application.internal.outboundservices.hashing.HashingService;
import com.agrotech.api.iam.application.internal.outboundservices.tokens.TokenService;
import com.agrotech.api.iam.domain.exceptions.InvalidPasswordException;
import com.agrotech.api.iam.domain.exceptions.InvalidRoleException;
import com.agrotech.api.iam.domain.exceptions.UserNotFoundInSignInException;
import com.agrotech.api.iam.domain.exceptions.UsernameAlreadyExistsException;
import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.iam.domain.model.commands.SignInCommand;
import com.agrotech.api.iam.domain.model.commands.SignUpCommand;
import com.agrotech.api.iam.domain.services.UserCommandService;
import com.agrotech.api.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.agrotech.api.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * User command service implementation
 * <p>
 *     This class implements the {@link UserCommandService} interface and provides the implementation for the
 *     {@link SignInCommand} and {@link SignUpCommand} commands.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;
    private final ExternalProfileRoleService externalProfileRoleService;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RoleRepository roleRepository, ExternalProfileRoleService externalProfileRoleService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
        this.externalProfileRoleService = externalProfileRoleService;
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username());
        if (user.isEmpty()) throw new UserNotFoundInSignInException(command.username());
        if (!hashingService.matches(command.password(), user.get().getPassword())) throw new InvalidPasswordException();
        var token = tokenService.generateToken(user.get().getUsername());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    @Override
    @Transactional
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username())) throw new UsernameAlreadyExistsException();
        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new InvalidRoleException(role.getStringName()))).toList();
        var user = new User(command.username(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        //Create a farmer or advisor depending on the role
        roles.forEach(role -> {
            if (role.getStringName().equals("ROLE_FARMER")) {
                externalProfileRoleService.createFarmer(user.getId(), user);
            }
            if (role.getStringName().equals("ROLE_ADVISOR")) {
                externalProfileRoleService.createAdvisor(user.getId(), user);
            }
        });
        return userRepository.findByUsername(command.username());
    }
}
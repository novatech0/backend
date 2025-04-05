package com.agrotech.api.iam.application.internal.commandservices;

import com.agrotech.api.iam.application.internal.outboundservices.acl.ExternalProfileRoleService;
import com.agrotech.api.iam.application.internal.outboundservices.hashing.HashingService;
import com.agrotech.api.iam.application.internal.outboundservices.tokens.TokenService;
import com.agrotech.api.iam.domain.exceptions.RoleNotFoundException;
import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.iam.domain.model.commands.SignInCommand;
import com.agrotech.api.iam.domain.model.commands.SignUpCommand;
import com.agrotech.api.iam.domain.services.UserCommandService;
import com.agrotech.api.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.agrotech.api.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

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

    /**
     * Handle the sign-in command
     * <p>
     *     This method handles the {@link SignInCommand} command and returns the user and the token.
     * </p>
     * @param command the sign-in command containing the username and password
     * @return and optional containing the user matching the username and the generated token
     * @throws RuntimeException if the user is not found or the password is invalid
     */
    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username());
        if (user.isEmpty())
            throw new RuntimeException("User not found");
        if (!hashingService.matches(command.password(), user.get().getPassword()))
            throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(user.get().getUsername());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    /**
     * Handle the sign-up command
     * <p>
     *     This method handles the {@link SignUpCommand} command and returns the user.
     * </p>
     * @param command the sign-up command containing the username and password
     * @return the created user
     */
    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username()))
            throw new RuntimeException("Username already exists");
        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RoleNotFoundException(role.getStringName()))).toList();
        var user = new User(command.username(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        //Create farmer or advisor depending on the role
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
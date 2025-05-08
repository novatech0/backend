package com.agrotech.api.iam.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.*;

import com.agrotech.api.iam.domain.model.entities.Role;
import com.agrotech.api.iam.domain.model.queries.GetAllRolesQuery;
import com.agrotech.api.iam.domain.model.valueobjects.Roles;
import com.agrotech.api.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class RoleQueryServiceImplTest {
    // handle(GetAllRolesQuery) returns all roles from repository
    @Test
    public void test_handle_get_all_roles_query_returns_all_roles() {
        // Arrange
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        List<Role> expectedRoles = List.of(
                new Role(Roles.ROLE_ADMIN),
                new Role(Roles.ROLE_USER)
        );
        Mockito.when(roleRepository.findAll()).thenReturn(expectedRoles);

        RoleQueryServiceImpl roleQueryService = new RoleQueryServiceImpl(roleRepository);
        GetAllRolesQuery query = new GetAllRolesQuery();

        // Act
        List<Role> actualRoles = roleQueryService.handle(query);

        // Assert
        assertEquals(expectedRoles, actualRoles);
        Mockito.verify(roleRepository).findAll();
    }

    // handle(GetAllRolesQuery) with null query parameter
    @Test
    public void test_handle_get_all_roles_query_with_null_parameter() {
        // Arrange
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        List<Role> expectedRoles = List.of(
                new Role(Roles.ROLE_USER)
        );
        Mockito.when(roleRepository.findAll()).thenReturn(expectedRoles);

        RoleQueryServiceImpl roleQueryService = new RoleQueryServiceImpl(roleRepository);

        // Act
        List<Role> actualRoles = roleQueryService.handle((GetAllRolesQuery) null);

        // Assert
        assertEquals(expectedRoles, actualRoles);
        Mockito.verify(roleRepository).findAll();
    }
}
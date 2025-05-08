package com.agrotech.api.iam.domain.model.aggregates;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import java.util.Set;

class UserTest {
    // Creating a User with username and password initializes empty roles and notifications collections
    @Test
    public void test_user_constructor_initializes_empty_collections() {
        // Arrange
        String username = "test@example.com";
        String password = "password123";

        // Act
        User user = new User(username, password);

        // Assert
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
        assertNotNull(user.getNotifications());
        assertTrue(user.getNotifications().isEmpty());
    }
    // Username validation fails when not an email format
    @Test
    public void test_username_validation_fails_when_not_email_format() {
        // Arrange
        User user = new User();
        user.setUsername("invalid-email");
        user.setPassword("password123");

        // Act & Assert
        Set<ConstraintViolation<User>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        boolean hasEmailViolation = violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("username") &&
                        violation.getMessage().equals("Username must be an email"));
        assertTrue(hasEmailViolation);
    }


}
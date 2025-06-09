package com.agrotech.api.management.interfaces.rest;

import com.agrotech.api.AgrotechApplication;
import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.iam.domain.model.commands.SignInCommand;
import com.agrotech.api.iam.domain.model.commands.SignUpCommand;
import com.agrotech.api.iam.domain.model.entities.Role;
import com.agrotech.api.iam.domain.services.UserCommandService;
import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import com.agrotech.api.management.domain.model.commands.CreateEnclosureCommand;
import com.agrotech.api.management.infrastructure.persistence.jpa.repositories.EnclosureRepository;
import com.agrotech.api.management.interfaces.rest.resources.CreateEnclosureResource;
import com.agrotech.api.management.interfaces.rest.resources.UpdateEnclosureResource;
import com.agrotech.api.profile.domain.model.commands.CreateFarmerCommand;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.profile.infrastructure.persistence.jpa.repositories.FarmerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AgrotechApplication.class)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class EnclosuresControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EnclosureRepository enclosureRepository;
    @Autowired
    private FarmerRepository farmerRepository;
    @Autowired
    private UserCommandService userCommandService;

    private String token;
    private Farmer farmer;

    @BeforeEach
    void setup() {
        Optional<User> user = userCommandService.handle(new SignUpCommand("testuser@example.com", "password", List.of(Role.getDefaultRole())));
        String token = userCommandService.handle(new SignInCommand("testuser@example.com", "password")).orElseThrow().getRight();
        Farmer farmer = farmerRepository.save(new Farmer(new CreateFarmerCommand(user.get().getId()), user.get()));

        this.token = token;
        this.farmer = farmer;
    }

    @Test
    void postEnclosure() throws Exception {
        // Arrange
        CreateEnclosureResource enclosure = new CreateEnclosureResource("Enclosure 1", 100, "Grassland", farmer.getId());

        // Act
        mockMvc.perform(post("/api/v1/enclosures")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(enclosure)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Enclosure 1"))
                .andExpect(jsonPath("$.capacity").value(100.0))
                .andExpect(jsonPath("$.type").value("Grassland"));
    }

    @Test
    void postEnclosureWithInvalidToken() throws Exception {
        // Arrange
        CreateEnclosureResource enclosure = new CreateEnclosureResource("Enclosure 1", 100, "Grassland", 1L);

        // Act
        mockMvc.perform(post("/api/v1/enclosures")
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(enclosure)))
                // Assert
                .andExpect(status().isUnauthorized());
    }

    @Test
    void postEnclosureWithInvalidData() throws Exception {
        // Arrange
        CreateEnclosureResource enclosure = new CreateEnclosureResource("", -1, "", farmer.getId());

        // Act
        mockMvc.perform(post("/api/v1/enclosures")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(enclosure)))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void postEnclosureWithNonExistentFarmer() throws Exception {
        // Arrange
        CreateEnclosureResource enclosure = new CreateEnclosureResource("Enclosure 1", 100, "Grassland", 999L); // Non-existent farmer ID
        // Act
        mockMvc.perform(post("/api/v1/enclosures")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(enclosure)))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Farmer with id 999 not found"));
    }

    @Test
    void getEnclosures() throws Exception {
        // Arrange
        CreateEnclosureCommand enclosure1 = new CreateEnclosureCommand("Enclosure 1", 100, "Grassland", farmer.getId());
        CreateEnclosureCommand enclosure2 = new CreateEnclosureCommand("Enclosure 2", 200, "Forest", farmer.getId());

        enclosureRepository.save(new Enclosure(enclosure1, farmer));
        enclosureRepository.save(new Enclosure(enclosure2, farmer));

        // Act
        mockMvc.perform(get("/api/v1/enclosures")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Enclosure 1"))
                .andExpect(jsonPath("$[0].capacity").value(100.0))
                .andExpect(jsonPath("$[0].type").value("Grassland"))
                .andExpect(jsonPath("$[1].name").value("Enclosure 2"))
                .andExpect(jsonPath("$[1].capacity").value(200.0))
                .andExpect(jsonPath("$[1].type").value("Forest"));
    }

    @Test
    void getEnclosuresWithInvalidToken() throws Exception {
        // Act
        mockMvc.perform(get("/api/v1/enclosures")
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token"))
                // Assert
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getEnclosureById() throws Exception {
        // Arrange
        CreateEnclosureCommand enclosure1 = new CreateEnclosureCommand("Enclosure 1", 100, "Grassland", farmer.getId());
        CreateEnclosureCommand enclosure2 = new CreateEnclosureCommand("Enclosure 2", 200, "Forest", farmer.getId());
        Enclosure enclosureEntity1 = enclosureRepository.save(new Enclosure(enclosure1, farmer));
        Enclosure enclosureEntity2 = enclosureRepository.save(new Enclosure(enclosure2, farmer));

        // Act
        mockMvc.perform(get("/api/v1/enclosures/" + enclosureEntity1.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Enclosure 1"))
                .andExpect(jsonPath("$.capacity").value(100.0))
                .andExpect(jsonPath("$.type").value("Grassland"));

        mockMvc.perform(get("/api/v1/enclosures/" + enclosureEntity2.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Enclosure 2"))
                .andExpect(jsonPath("$.capacity").value(200.0))
                .andExpect(jsonPath("$.type").value("Forest"));
    }

    @Test
    void getEnclosureByIdWithNonExistentId() throws Exception {
        // Arrange
        CreateEnclosureCommand enclosure1 = new CreateEnclosureCommand("Enclosure 1", 100, "Grassland", farmer.getId());
        CreateEnclosureCommand enclosure2 = new CreateEnclosureCommand("Enclosure 2", 200, "Forest", farmer.getId());
        Enclosure enclosureEntity1 = enclosureRepository.save(new Enclosure(enclosure1, farmer));
        Enclosure enclosureEntity2 = enclosureRepository.save(new Enclosure(enclosure2, farmer));

        // Act
        mockMvc.perform(get("/api/v1/enclosures/999") // Non-existent ID
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEnclosure() throws Exception {
        // Arrange
        CreateEnclosureCommand enclosure1 = new CreateEnclosureCommand("Enclosure 1", 100, "Grassland", farmer.getId());
        Enclosure enclosureEntity1 = enclosureRepository.save(new Enclosure(enclosure1, farmer));

        // Act
        UpdateEnclosureResource updatedEnclosure = new UpdateEnclosureResource("Updated Enclosure", 150, "Desert");
        mockMvc.perform(put("/api/v1/enclosures/" + enclosureEntity1.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updatedEnclosure)))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Enclosure"))
                .andExpect(jsonPath("$.capacity").value(150.0))
                .andExpect(jsonPath("$.type").value("Desert"));
    }

    @Test
    void updateEnclosureWithInvalidToken() throws Exception {
        // Arrange
        UpdateEnclosureResource updatedEnclosure = new UpdateEnclosureResource("Updated Enclosure", 150, "Desert");

        // Act
        mockMvc.perform(put("/api/v1/enclosures/1") // Assuming 1 is a valid ID
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(updatedEnclosure)))
                // Assert
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateEnclosureWithNonExistentId() throws Exception {
        // Arrange
        UpdateEnclosureResource updatedEnclosure = new UpdateEnclosureResource("Updated Enclosure", 150, "Desert");
        // Act
        mockMvc.perform(put("/api/v1/enclosures/999") // Non-existent ID
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updatedEnclosure)))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enclosure with id 999 not found"));
    }

    @Test
    void deleteEnclosure() throws Exception {
        // Arrange
        CreateEnclosureCommand enclosure1 = new CreateEnclosureCommand("Enclosure 1", 100, "Grassland", farmer.getId());
        Enclosure enclosureEntity1 = enclosureRepository.save(new Enclosure(enclosure1, farmer));
        // Act
        mockMvc.perform(delete("/api/v1/enclosures/" + enclosureEntity1.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk());

        // Verify that the enclosure is deleted
        assertFalse(enclosureRepository.findById(enclosureEntity1.getId()).isPresent());
    }

    @Test
    void deleteEnclosureWithInvalidToken() throws Exception {
        // Act
        mockMvc.perform(delete("/api/v1/enclosures/1") // Assuming 1 is a valid ID
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token"))
                // Assert
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteEnclosureWithNonExistentId() throws Exception {
        // Act
        mockMvc.perform(delete("/api/v1/enclosures/999") // Non-existent ID
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enclosure with id 999 not found"));
    }
}
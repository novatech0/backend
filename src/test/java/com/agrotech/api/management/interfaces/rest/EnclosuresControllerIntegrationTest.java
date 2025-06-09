package com.agrotech.api.management.interfaces.rest;

import com.agrotech.api.AgrotechApplication;
import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.iam.domain.model.commands.SignInCommand;
import com.agrotech.api.iam.domain.model.commands.SignUpCommand;
import com.agrotech.api.iam.domain.model.entities.Role;
import com.agrotech.api.iam.domain.services.UserCommandService;
import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import com.agrotech.api.management.domain.model.commands.CreateEnclosureCommand;
import com.agrotech.api.management.infrastructure.persistence.jpa.entities.EnclosureEntity;
import com.agrotech.api.management.infrastructure.persistence.jpa.mappers.EnclosureMapper;
import com.agrotech.api.management.infrastructure.persistence.jpa.repositories.EnclosureRepository;
import com.agrotech.api.management.interfaces.rest.resources.CreateEnclosureResource;
import com.agrotech.api.management.interfaces.rest.resources.UpdateEnclosureResource;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.profile.infrastructure.persistence.jpa.mappers.FarmerMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        Farmer farmer = FarmerMapper.toDomain(farmerRepository.save(FarmerMapper.toEntity(new Farmer(user.get()))));

        this.token = token;
        this.farmer = farmer;
    }

    @Test
    void postEnclosure() throws Exception {
        CreateEnclosureResource resource = new CreateEnclosureResource("Barn", 10, "Barn Type", 1L);

        mockMvc.perform(post("/api/v1/enclosures")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Barn"))
                .andExpect(jsonPath("$.capacity").value(10))
                .andExpect(jsonPath("$.species").value("Sheep"));
    }

    @Test
    void getEnclosures() throws Exception {
        // Crear enclosure asociado
        Enclosure enclosure = new Enclosure(new CreateEnclosureCommand("Stable", 6, "Horse", farmer.getId()), farmer);
        enclosureRepository.save(EnclosureMapper.toEntity(enclosure));

        mockMvc.perform(get("/api/v1/enclosures")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Stable"));
    }

    @Test
    void updateEnclosure() throws Exception {
        Enclosure enclosure = new Enclosure(new CreateEnclosureCommand("Old Corral", 3, "Pig", farmer.getId()), farmer);
        EnclosureEntity savedEntity = enclosureRepository.save(EnclosureMapper.toEntity(enclosure));

        UpdateEnclosureResource updateResource = new UpdateEnclosureResource("New Corral", 5, "Pig");

        mockMvc.perform(put("/api/v1/enclosures/" + savedEntity.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Corral"))
                .andExpect(jsonPath("$.capacity").value(5));
    }

    @Test
    void deleteEnclosure() throws Exception {
        Enclosure enclosure = new Enclosure(new CreateEnclosureCommand("Temporary", 2, "Goat", farmer.getId()), farmer);
        EnclosureEntity savedEntity = enclosureRepository.save(EnclosureMapper.toEntity(enclosure));

        mockMvc.perform(delete("/api/v1/enclosures/" + savedEntity.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertFalse(enclosureRepository.findById(savedEntity.getId()).isPresent());
    }

    @Test
    void postEnclosureWithInvalidToken() throws Exception {
        CreateEnclosureResource resource = new CreateEnclosureResource("Invalid", 3, "Rare Type", 1L);

        mockMvc.perform(post("/api/v1/enclosures")
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getEnclosuresWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/v1/enclosures")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateEnclosureWithInvalidToken() throws Exception {
        UpdateEnclosureResource update = new UpdateEnclosureResource("Invalid", 1, "Sheep");

        mockMvc.perform(put("/api/v1/enclosures/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteEnclosureWithInvalidToken() throws Exception {
        mockMvc.perform(delete("/api/v1/enclosures/1")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }
}

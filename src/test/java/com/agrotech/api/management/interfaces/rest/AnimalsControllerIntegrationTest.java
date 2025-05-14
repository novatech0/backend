package com.agrotech.api.management.interfaces.rest;

import com.agrotech.api.AgrotechApplication;
import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.iam.domain.model.commands.SignInCommand;
import com.agrotech.api.iam.domain.model.commands.SignUpCommand;
import com.agrotech.api.iam.domain.model.entities.Role;
import com.agrotech.api.iam.domain.services.UserCommandService;
import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import com.agrotech.api.management.domain.model.commands.CreateAnimalCommand;
import com.agrotech.api.management.domain.model.commands.CreateEnclosureCommand;
import com.agrotech.api.management.domain.model.entities.Animal;
import com.agrotech.api.management.infrastructure.persitence.jpa.repositories.AnimalRepository;
import com.agrotech.api.management.infrastructure.persitence.jpa.repositories.EnclosureRepository;
import com.agrotech.api.management.interfaces.rest.resources.CreateAnimalResource;
import com.agrotech.api.profile.domain.model.commands.CreateFarmerCommand;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.profile.infrastructure.persistence.jpa.repositories.FarmerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest(classes = AgrotechApplication.class)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AnimalsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private EnclosureRepository enclosureRepository;
    @Autowired
    private FarmerRepository farmerRepository;
    @Autowired
    private UserCommandService userCommandService;

    @Test
    void postAnimal() throws Exception {
        // Arrange
        Optional<User> user = userCommandService.handle(new SignUpCommand("testuser@example.com", "password", List.of(Role.getDefaultRole())));
        String token = userCommandService.handle(new SignInCommand("testuser@example.com", "password")).orElseThrow().getRight();
        Farmer farmer = farmerRepository.save(new Farmer(new CreateFarmerCommand(user.get().getId()), user.get()));
        Enclosure enclosure = enclosureRepository.save(new Enclosure(new CreateEnclosureCommand("Bella", 3, "Cow", farmer.getId()), farmer));

        CreateAnimalResource animal = new CreateAnimalResource(
                "Bella", 3, "Cow", "Holstein", false, 450.0f, "HEALTHY", enclosure.getId()
        );

        // Act
        mockMvc.perform(post("/api/v1/animals")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(animal)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bella"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.species").value("Cow"))
                .andExpect(jsonPath("$.breed").value("Holstein"))
                .andExpect(jsonPath("$.gender").value(false))
                .andExpect(jsonPath("$.weight").value(450.0))
                .andExpect(jsonPath("$.health").value("HEALTHY"));
    }

    @Test
    void postAnimalWithInvalidToken() throws Exception {
        // Arrange
        CreateAnimalResource animal = new CreateAnimalResource(
                "Test", 2, "Cow", "Holstein", false, 400.0f, "HEALTHY", 1L
        );

        // Act
        mockMvc.perform(post("/api/v1/animals")
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(animal)))
                // Assert
                .andExpect(status().isUnauthorized());
    }


    @Test
    void getAnimals() throws Exception {
        // Arrange
        Optional<User> user = userCommandService.handle(new SignUpCommand("getuser@example.com", "password", List.of(Role.getDefaultRole())));
        String token = userCommandService.handle(new SignInCommand("getuser@example.com", "password")).orElseThrow().getRight();
        Farmer farmer = farmerRepository.save(new Farmer(new CreateFarmerCommand(user.get().getId()), user.get()));
        Enclosure enclosure = enclosureRepository.save(new Enclosure(new CreateEnclosureCommand("Paddock", 2, "Sheep", farmer.getId()), farmer));

        animalRepository.save(new Animal(new CreateAnimalCommand("Luna", 2, "Sheep", "Merino", true, 70.5f, "HEALTHY", enclosure.getId()), enclosure));
        animalRepository.save(new Animal(new CreateAnimalCommand("Leo", 4, "Sheep", "Suffolk", false, 80.0f, "HEALTHY", enclosure.getId()), enclosure));


        // Act
        mockMvc.perform(get("/api/v1/animals")
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Luna"))
                .andExpect(jsonPath("$[0].age").value(2))
                .andExpect(jsonPath("$[0].species").value("Sheep"))
                .andExpect(jsonPath("$[0].breed").value("Merino"))
                .andExpect(jsonPath("$[1].name").value("Leo"))
                .andExpect(jsonPath("$[1].age").value(4))
                .andExpect(jsonPath("$[1].species").value("Sheep"))
                .andExpect(jsonPath("$[1].breed").value("Suffolk"));
    }

    @Test
    void getAnimalsWithInvalidToken() throws Exception {
        // Act
        mockMvc.perform(get("/api/v1/animals")
                        .header("Authorization", "Bearer invalid_token"))
                // Assert
                .andExpect(status().isUnauthorized());
    }


    @Test
    void deleteAnimal() throws Exception {
        // Arrange
        Optional<User> user = userCommandService.handle(new SignUpCommand("deleteuser@example.com", "password", List.of(Role.getDefaultRole())));
        String token = userCommandService.handle(new SignInCommand("deleteuser@example.com", "password")).orElseThrow().getRight();
        Farmer farmer = farmerRepository.save(new Farmer(new CreateFarmerCommand(user.get().getId()), user.get()));
        Enclosure enclosure = enclosureRepository.save(new Enclosure(new CreateEnclosureCommand("Corral", 4, "Pig", farmer.getId()), farmer));

        var animal = animalRepository.save(new Animal(
                new CreateAnimalCommand("Toby", 1, "Pig", "Large White", true, 90.0f, "SICK", enclosure.getId()), enclosure));

        // Act
        mockMvc.perform(delete("/api/v1/animals/" + animal.getId())
                        .header("Authorization", "Bearer " + token))
                // Assert
                .andExpect(status().isOk());

        assertFalse(animalRepository.findById(animal.getId()).isPresent());
    }

    @Test
    void deleteAnimalWithInvalidToken() throws Exception {
        // Act
        mockMvc.perform(delete("/api/v1/animals/1")
                        .header("Authorization", "Bearer invalid_token"))
                // Assert
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateAnimal() throws Exception {
        // Arrange
        Optional<User> user = userCommandService.handle(new SignUpCommand("updateuser@example.com", "password", List.of(Role.getDefaultRole())));
        String token = userCommandService.handle(new SignInCommand("updateuser@example.com", "password")).orElseThrow().getRight();
        Farmer farmer = farmerRepository.save(new Farmer(new CreateFarmerCommand(user.get().getId()), user.get()));
        Enclosure enclosure = enclosureRepository.save(new Enclosure(new CreateEnclosureCommand("Stable", 5, "Horse", farmer.getId()), farmer));

        var animal = animalRepository.save(new Animal(
                new CreateAnimalCommand("Max", 4, "Horse", "Arabian", true, 500.0f, "HEALTHY", enclosure.getId()), enclosure));

        CreateAnimalResource updatedAnimal = new CreateAnimalResource(
                "Maximus", 5, "Horse", "Thoroughbred", true, 520.0f, "HEALTHY", enclosure.getId()
        );

        // Act
        mockMvc.perform(put("/api/v1/animals/" + animal.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updatedAnimal)))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maximus"))
                .andExpect(jsonPath("$.age").value(5))
                .andExpect(jsonPath("$.breed").value("Thoroughbred"))
                .andExpect(jsonPath("$.weight").value(520.0));
    }

    @Test
    void updateAnimalWithInvalidToken() throws Exception {
        // Arrange
        CreateAnimalResource updatedAnimal = new CreateAnimalResource(
                "Invalid", 1, "Pig", "Mixed", true, 80.0f, "SICK", 1L
        );
        // Act
        mockMvc.perform(put("/api/v1/animals/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(updatedAnimal)))
                // Assert
                .andExpect(status().isUnauthorized());
    }


}
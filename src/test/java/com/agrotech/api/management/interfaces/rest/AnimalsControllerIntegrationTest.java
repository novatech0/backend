package com.agrotech.api.management.interfaces.rest;

import com.agrotech.api.AgrotechApplication;
import com.agrotech.api.iam.domain.model.aggregates.User;
import com.agrotech.api.iam.domain.model.commands.SignInCommand;
import com.agrotech.api.iam.domain.model.commands.SignUpCommand;
import com.agrotech.api.iam.domain.model.entities.Role;
import com.agrotech.api.iam.domain.services.UserCommandService;
import com.agrotech.api.iam.infrastructure.persistence.jpa.entities.UserEntity;
import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import com.agrotech.api.management.domain.model.commands.CreateAnimalCommand;
import com.agrotech.api.management.domain.model.commands.CreateEnclosureCommand;
import com.agrotech.api.management.domain.model.entities.Animal;
import com.agrotech.api.management.domain.model.valueobjects.HealthStatus;
import com.agrotech.api.management.infrastructure.persistence.jpa.entities.AnimalEntity;
import com.agrotech.api.management.infrastructure.persistence.jpa.entities.EnclosureEntity;
import com.agrotech.api.management.infrastructure.persistence.jpa.mappers.EnclosureMapper;
import com.agrotech.api.management.infrastructure.persistence.jpa.repositories.AnimalRepository;
import com.agrotech.api.management.infrastructure.persistence.jpa.repositories.EnclosureRepository;
import com.agrotech.api.management.interfaces.rest.resources.CreateAnimalResource;
import com.agrotech.api.profile.domain.model.commands.CreateFarmerCommand;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.profile.infrastructure.persistence.jpa.entities.FarmerEntity;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    private String token;
    private Farmer farmer;

    @BeforeEach
    void setup() {
        Optional<User> user = userCommandService.handle(new SignUpCommand("getuser@example.com", "password", List.of(Role.getDefaultRole())));
        String token = userCommandService.handle(new SignInCommand("getuser@example.com", "password")).orElseThrow().getRight();
        Farmer farmer = FarmerMapper.toDomain(farmerRepository.save(FarmerMapper.toEntity(new Farmer(user.get()))));

        this.token = token;
        this.farmer = farmer;
    }

    @Test
    void postAnimal() throws Exception {
        // Convertimos a Entity para persistir
        Enclosure enclosure = new Enclosure(new CreateEnclosureCommand("Paddock", 2, "Cow", farmer.getId()), farmer);
        EnclosureEntity savedEntity = enclosureRepository.save(EnclosureMapper.toEntity(enclosure));
        Enclosure savedEnclosure = EnclosureMapper.toDomain(savedEntity);

        CreateAnimalResource animal = new CreateAnimalResource(
                "Bella", 3, "Cow", "Holstein", false, 450.0f, "HEALTHY", savedEnclosure.getId()
        );

        mockMvc.perform(post("/api/v1/animals")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(animal)))
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
        CreateAnimalResource animal = new CreateAnimalResource(
                "Test", 2, "Cow", "Holstein", false, 400.0f, "HEALTHY", 1L
        );

        mockMvc.perform(post("/api/v1/animals")
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(animal)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAnimals() throws Exception {
        // Create a UserEntity instance
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("juan@mail.com");
        userEntity.setPassword("123456789");

        // Save the UserEntity and use it to create a FarmerEntity
        FarmerEntity savedFarmer = farmerRepository.save(new FarmerEntity(null, userEntity));

        // Create and save the EnclosureEntity with the associated FarmerEntity
        EnclosureEntity savedEnclosure = enclosureRepository.save(
                new EnclosureEntity(null, "Paddock", 2, "Sheep", savedFarmer)
        );

        // Save two animals related to the enclosure
        animalRepository.save(new AnimalEntity(null, "Luna", 2, "Sheep", "Merino", true, 70.5f, HealthStatus.HEALTHY, savedEnclosure));
        animalRepository.save(new AnimalEntity(null, "Leo", 4, "Sheep", "Suffolk", false, 80.0f, HealthStatus.HEALTHY, savedEnclosure));

        // Execute the endpoint and verify the response
        mockMvc.perform(get("/api/v1/animals")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Luna"))
                .andExpect(jsonPath("$[1].name").value("Leo"));
    }




    @Test
    void getAnimalsWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/v1/animals")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAnimal() throws Exception {
        // Crear EnclosureEntity con FarmerEntity
        Enclosure enclosure = new Enclosure(new CreateEnclosureCommand("Corral", 4, "Pig", farmer.getId()), farmer);
        EnclosureEntity savedEnclosure = enclosureRepository.save(EnclosureMapper.toEntity(enclosure));

        // Crear y guardar AnimalEntity
        AnimalEntity animal = animalRepository.save(new AnimalEntity(
                null, "Toby", 1, "Pig", "Large White", true, 90.0f, HealthStatus.SICK, savedEnclosure
        ));

        // Ejecutar DELETE
        mockMvc.perform(delete("/api/v1/animals/" + animal.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Verificar que se eliminó
        assertFalse(animalRepository.findById(animal.getId()).isPresent());
    }

    @Test
    void deleteAnimalWithInvalidToken() throws Exception {
        mockMvc.perform(delete("/api/v1/animals/1")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateAnimal() throws Exception {
        // Crear EnclosureEntity con FarmerEntity
        Enclosure enclosure = new Enclosure(new CreateEnclosureCommand("Stable", 5, "Horse", farmer.getId()), farmer);
        EnclosureEntity savedEnclosure = enclosureRepository.save(EnclosureMapper.toEntity(enclosure));

        // Crear y guardar AnimalEntity
        AnimalEntity animal = animalRepository.save(new AnimalEntity(
                null, "Max", 4, "Horse", "Arabian", true, 500.0f, HealthStatus.HEALTHY, savedEnclosure
        ));

        // Crear recurso con nuevos datos
        CreateAnimalResource updatedAnimal = new CreateAnimalResource(
                "Maximus", 5, "Horse", "Thoroughbred", true, 520.0f, "HEALTHY", savedEnclosure.getId()
        );

        // Ejecutar PUT
        mockMvc.perform(put("/api/v1/animals/" + animal.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updatedAnimal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maximus"))
                .andExpect(jsonPath("$.age").value(5))
                .andExpect(jsonPath("$.breed").value("Thoroughbred"))
                .andExpect(jsonPath("$.weight").value(520.0));
    }


    @Test
    void updateAnimalWithInvalidToken() throws Exception {
        CreateAnimalResource updatedAnimal = new CreateAnimalResource(
                "Invalid", 1, "Pig", "Mixed", true, 80.0f, "SICK", 1L
        );

        mockMvc.perform(put("/api/v1/animals/1")
                        .contentType("application/json")
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(updatedAnimal)))
                .andExpect(status().isUnauthorized());
    }
}

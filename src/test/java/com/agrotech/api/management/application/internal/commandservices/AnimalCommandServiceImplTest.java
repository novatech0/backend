package com.agrotech.api.management.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import com.agrotech.api.management.domain.model.commands.CreateAnimalCommand;
import com.agrotech.api.management.domain.model.entities.Animal;
import com.agrotech.api.management.infrastructure.persitence.jpa.repositories.AnimalRepository;
import com.agrotech.api.management.infrastructure.persitence.jpa.repositories.EnclosureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class AnimalCommandServiceImplTest {
    @Mock
    private AnimalRepository animalRepository;
    @Mock
    private EnclosureRepository enclosureRepository;
    @Mock
    private AnimalCommandServiceImpl animalCommandService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    // Successfully create a new animal with valid CreateAnimalCommand and existing enclosure
    @Test
    public void test_handle_create_animal_command_with_valid_data_returns_animal_id() {
        // Arrange
        Long enclosureId = 1L;
        Long expectedAnimalId = 1L;

        CreateAnimalCommand command = new CreateAnimalCommand(
                "Bessie",
                3,
                "Cow",
                "Holstein",
                true,
                450.5f,
                "HEALTHY",
                enclosureId
        );

        Enclosure mockEnclosure = Mockito.mock(Enclosure.class);
        Animal mockAnimal = Mockito.mock(Animal.class);

        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(mockEnclosure));
        when(animalRepository.save(any(Animal.class))).thenAnswer(invocation -> {
            Animal savedAnimal = invocation.getArgument(0);
            when(savedAnimal.getId()).thenReturn(expectedAnimalId);
            return savedAnimal;
        });

        // Act
        Long actualAnimalId = animalCommandService.handle(command);

        // Assert
        assertEquals(expectedAnimalId, actualAnimalId);
        verify(animalRepository).save(any(Animal.class));
        verify(enclosureRepository).findById(enclosureId);
    }
}
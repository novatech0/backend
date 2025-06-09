package com.agrotech.api.management.application.internal.commandservices;
import com.agrotech.api.management.domain.model.aggregates.Enclosure;
import com.agrotech.api.management.domain.model.commands.CreateEnclosureCommand;
import com.agrotech.api.management.infrastructure.persistence.jpa.repositories.EnclosureRepository;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import com.agrotech.api.profile.infrastructure.persistence.jpa.repositories.FarmerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
class EnclosureCommandServiceImplTest {
    @Mock
    private  EnclosureRepository enclosureRepository;
    @Mock
    private  FarmerRepository farmerRepository;
    @InjectMocks
    private  EnclosureCommandServiceImpl enclosureCommandService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    // Creating an enclosure with valid data returns the enclosure ID
    @Test
    public void test_handle_create_enclosure_command_returns_enclosure_id() {
        // Arrange
        Long farmerId = 1L;
        Long enclosureId = 1L;
        String name = "Cattle Pen";
        Integer capacity = 50;
        String type = "Cattle";

        CreateEnclosureCommand command = new CreateEnclosureCommand(name, capacity, type, farmerId);

        Farmer farmer = Mockito.mock(Farmer.class);
        Enclosure enclosure = Mockito.mock(Enclosure.class);

        when(farmerRepository.findById(farmerId)).thenReturn(Optional.of(farmer));
        when(enclosure.getId()).thenReturn(enclosureId);
        when(enclosureRepository.save(any(Enclosure.class))).thenReturn(enclosure);

        // Act
        Long result = enclosureCommandService.handle(command);

        // Assert
        assertEquals(enclosureId, result);
        Mockito.verify(farmerRepository).findById(farmerId);
        Mockito.verify(enclosureRepository).save(Mockito.any(Enclosure.class));

    }
}
package com.agrotech.api.appointment.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.agrotech.api.appointment.application.internal.outboundservices.acl.ExternalProfilesService;
import com.agrotech.api.appointment.domain.model.commands.CreateReviewCommand;
import com.agrotech.api.appointment.domain.model.entities.Review;
import com.agrotech.api.appointment.infrastructure.persistence.jpa.repositories.ReviewRepository;
import com.agrotech.api.profile.domain.model.entities.Advisor;
import com.agrotech.api.profile.domain.model.entities.Farmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

class ReviewCommandServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ExternalProfilesService externalProfilesService;

    @InjectMocks
    private ReviewCommandServiceImpl reviewCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_handle_create_review_command_success() {
        // Arrange
        Long advisorId = 1L;
        Long farmerId = 2L;
        String comment = "Great advice";
        Integer rating = 5;
        Long reviewId = 1L;

        // Creamos el comando que se pasará al servicio
        CreateReviewCommand command = new CreateReviewCommand(advisorId, farmerId, comment, rating);

        // Creamos mocks de los objetos Advisor y Farmer
        Advisor advisor = mock(Advisor.class);
        when(advisor.getId()).thenReturn(advisorId);

        Farmer farmer = mock(Farmer.class);
        when(farmer.getId()).thenReturn(farmerId);

        // Creamos un mock de Review, asignamos el ID y el advisorId que se devuelve en el método
        Review review = mock(Review.class);
        when(review.getId()).thenReturn(reviewId);
        when(review.getAdvisorId()).thenReturn(advisorId);

        // Simulamos las respuestas de los servicios externos
        when(externalProfilesService.fetchAdvisorById(advisorId)).thenReturn(Optional.of(advisor));
        when(externalProfilesService.fetchFarmerById(farmerId)).thenReturn(Optional.of(farmer));
        when(reviewRepository.findByAdvisor_IdAndFarmer_Id(advisorId, farmerId)).thenReturn(Optional.empty());

        // Simulamos que el repositorio guarda el review y lo devuelve
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // También simulamos que se encuentra el review por el advisorId
        when(reviewRepository.findByAdvisor_Id(advisorId)).thenReturn(java.util.List.of(review));

        // Simulamos que el servicio actualiza la calificación del advisor
        doNothing().when(externalProfilesService).updateRating(eq(advisorId), any(BigDecimal.class));

        // Act
        Long result = reviewCommandService.handle(command);

        // Assert
        // Verificamos que el ID del review guardado sea el esperado
        assertEquals(reviewId, result);

        // Verificamos que los métodos del repositorio y el servicio externo hayan sido llamados
        verify(reviewRepository).findByAdvisor_IdAndFarmer_Id(advisorId, farmerId);
        verify(externalProfilesService).fetchAdvisorById(advisorId);
        verify(externalProfilesService).fetchFarmerById(farmerId);
        verify(reviewRepository).save(any(Review.class));
        verify(reviewRepository).findByAdvisor_Id(advisorId);
        verify(externalProfilesService).updateRating(eq(advisorId), any(BigDecimal.class));
    }
}
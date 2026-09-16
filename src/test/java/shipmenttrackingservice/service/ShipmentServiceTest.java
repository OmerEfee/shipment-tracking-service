package shipmenttrackingservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shipmenttrackingservice.dto.CreateShipmentRequest;
import shipmenttrackingservice.dto.ShipmentResponse;
import shipmenttrackingservice.dto.UpdateShipmentStatusRequest;
import shipmenttrackingservice.entity.Shipment;
import shipmenttrackingservice.enums.ShipmentStatus;
import shipmenttrackingservice.exception.InvalidStateTransitionException;
import shipmenttrackingservice.exception.ResourceNotFoundException;
import shipmenttrackingservice.repository.ShipmentRepository;
import shipmenttrackingservice.service.state.ShipmentState;
import shipmenttrackingservice.service.state.ShipmentStateFactory;
import shipmenttrackingservice.util.TrackingNumberGenerator;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private TrackingNumberGenerator trackingNumberGenerator;

    @Mock
    private ShipmentStateFactory stateFactory;

    @Mock
    private ShipmentState mockState;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Test
    @DisplayName("Başarılı Kargo Oluşturma Senaryosu")
    void shouldCreateShipmentSuccessfully() {
        // Arrange (Hazırlık)
        CreateShipmentRequest request = CreateShipmentRequest.builder()
                .senderName("Ali Kaya")
                .receiverName("Veli Can")
                .receiverAddress("Ankara Kızılay Mah.")
                .build();

        String generatedTrackingNumber = "TRK-20260916-TEST1";

        when(trackingNumberGenerator.generate()).thenReturn(generatedTrackingNumber);
        when(shipmentRepository.existsByTrackingNumber(generatedTrackingNumber)).thenReturn(false);
        when(shipmentRepository.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act (Çalıştırma)
        ShipmentResponse response = shipmentService.createShipment(request);

        // Assert (Doğrulama)
        assertNotNull(response);
        assertEquals(generatedTrackingNumber, response.getTrackingNumber());
        assertEquals("Ali Kaya", response.getSenderName());
        assertEquals(ShipmentStatus.CREATED, response.getCurrentStatus());
        assertEquals(1, response.getHistory().size());

        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    @DisplayName("Olmayan Kargo Sorgulandığında ResourceNotFoundException Fırlatmalı")
    void shouldThrowExceptionWhenShipmentNotFound() {
        // Arrange
        String nonExistingTrackingNumber = "TRK-00000000-NONE";
        when(shipmentRepository.findByTrackingNumber(nonExistingTrackingNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            shipmentService.getShipmentByTrackingNumber(nonExistingTrackingNumber);
        });

        verify(shipmentRepository).findByTrackingNumber(nonExistingTrackingNumber);
    }

    @Test
    @DisplayName("Kural Dışı Durum Geçişinde InvalidStateTransitionException Fırlatmalı (State Pattern Testi)")
    void shouldThrowExceptionOnInvalidStateTransition() {
        // Arrange
        String trackingNumber = "TRK-20260916-TEST1";
        Shipment existingShipment = Shipment.builder()
                .trackingNumber(trackingNumber)
                .currentStatus(ShipmentStatus.CREATED)
                .build();

        UpdateShipmentStatusRequest updateRequest = UpdateShipmentStatusRequest.builder()
                .newStatus(ShipmentStatus.DELIVERED) // Kural dışı doğrudan teslimat isteği
                .location("Adres")
                .comment("Hızlı teslimat")
                .build();

        when(shipmentRepository.findByTrackingNumber(trackingNumber)).thenReturn(Optional.of(existingShipment));
        when(stateFactory.getState(ShipmentStatus.CREATED)).thenReturn(mockState);
        when(mockState.canTransitionTo(ShipmentStatus.DELIVERED)).thenReturn(false); // State Pattern geçişe izin vermiyor

        // Act & Assert
        assertThrows(InvalidStateTransitionException.class, () -> {
            shipmentService.updateShipmentStatus(trackingNumber, updateRequest);
        });

        // Veritabanına kayıt atılmadığını doğrula
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }
}

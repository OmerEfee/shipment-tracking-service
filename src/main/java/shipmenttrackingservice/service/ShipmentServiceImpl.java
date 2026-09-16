package shipmenttrackingservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shipmenttrackingservice.dto.CreateShipmentRequest;
import shipmenttrackingservice.dto.ShipmentResponse;
import shipmenttrackingservice.dto.ShipmentStatusLogResponse;
import shipmenttrackingservice.dto.UpdateShipmentStatusRequest;
import shipmenttrackingservice.entity.Shipment;
import shipmenttrackingservice.entity.ShipmentStatusLog;
import shipmenttrackingservice.enums.ShipmentStatus;
import shipmenttrackingservice.exception.InvalidStateTransitionException;
import shipmenttrackingservice.exception.ResourceNotFoundException;
import shipmenttrackingservice.repository.ShipmentRepository;
import shipmenttrackingservice.service.state.ShipmentState;
import shipmenttrackingservice.service.state.ShipmentStateFactory;
import shipmenttrackingservice.util.TrackingNumberGenerator;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final TrackingNumberGenerator trackingNumberGenerator;
    private final ShipmentStateFactory stateFactory;

    @Override
    @Transactional
    public ShipmentResponse createShipment(CreateShipmentRequest request) {
        // 1. Benzersiz Takip Numarası Üret (Çakışma varsa tekrar dene)
        String trackingNumber;
        do {
            trackingNumber = trackingNumberGenerator.generate();
        } while (shipmentRepository.existsByTrackingNumber(trackingNumber));

        // 2. Kargo Varlığını Oluştur
        Shipment shipment = Shipment.builder()
                .trackingNumber(trackingNumber)
                .senderName(request.getSenderName())
                .receiverName(request.getReceiverName())
                .receiverAddress(request.getReceiverAddress())
                .currentStatus(ShipmentStatus.CREATED)
                .build();

        // 3. İlk Durum Logunu Ekle (Audit Trail)
        ShipmentStatusLog initialLog = ShipmentStatusLog.builder()
                .status(ShipmentStatus.CREATED)
                .location("Kabul Şubesi")
                .comment("Kargo gönderi kaydı oluşturuldu.")
                .build();

        shipment.addStatusLog(initialLog);

        // 4. Veritabanına Kaydet
        Shipment savedShipment = shipmentRepository.save(shipment);

        return mapToResponse(savedShipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentByTrackingNumber(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Kargo bulunamadı! Takip No: " + trackingNumber));

        return mapToResponse(shipment);
    }

    @Override
    @Transactional
    public ShipmentResponse updateShipmentStatus(String trackingNumber, UpdateShipmentStatusRequest request) {
        // 1. Kargo var mı kontrolü
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Kargo bulunamadı! Takip No: " + trackingNumber));

        ShipmentStatus currentStatus = shipment.getCurrentStatus();
        ShipmentStatus nextStatus = request.getNewStatus();

        // 2. State Pattern ile Durum Geçiş Kontrolü
        ShipmentState currentState = stateFactory.getState(currentStatus);
        if (!currentState.canTransitionTo(nextStatus)) {
            throw new InvalidStateTransitionException(String.format(
                    "Geçersiz durum geçişi: '%s' durumundaki bir kargo '%s' durumuna geçirilemez!",
                    currentStatus.getDescription(),
                    nextStatus.getDescription()
            ));
        }

        // 3. Durumu Güncelle
        shipment.setCurrentStatus(nextStatus);

        // 4. Yeni Durum Tarihçe Logunu Ekle
        ShipmentStatusLog newLog = ShipmentStatusLog.builder()
                .status(nextStatus)
                .location(request.getLocation())
                .comment(request.getComment())
                .build();

        shipment.addStatusLog(newLog);

        // 5. Güncel Halini Kaydet
        Shipment updatedShipment = shipmentRepository.save(shipment);

        return mapToResponse(updatedShipment);
    }

    // Entity -> DTO Dönüştürücü Yardımcı Metot
    private ShipmentResponse mapToResponse(Shipment shipment) {
        List<ShipmentStatusLogResponse> logResponses = shipment.getStatusLogs().stream()
                .map(log -> ShipmentStatusLogResponse.builder()
                        .status(log.getStatus())
                        .statusDescription(log.getStatus().getDescription())
                        .location(log.getLocation())
                        .comment(log.getComment())
                        .timestamp(log.getTimestamp())
                        .build())
                .collect(Collectors.toList());

        return ShipmentResponse.builder()
                .trackingNumber(shipment.getTrackingNumber())
                .senderName(shipment.getSenderName())
                .receiverName(shipment.getReceiverName())
                .receiverAddress(shipment.getReceiverAddress())
                .currentStatus(shipment.getCurrentStatus())
                .currentStatusDescription(shipment.getCurrentStatus().getDescription())
                .createdAt(shipment.getCreatedAt())
                .updatedAt(shipment.getUpdatedAt())
                .history(logResponses)
                .build();
    }
}
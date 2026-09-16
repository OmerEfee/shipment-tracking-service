package shipmenttrackingservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shipmenttrackingservice.dto.CreateShipmentRequest;
import shipmenttrackingservice.dto.ShipmentResponse;
import shipmenttrackingservice.dto.UpdateShipmentStatusRequest;
import shipmenttrackingservice.service.ShipmentService;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    // 1. Yeni Kargo Oluşturma Uç Noktası
    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        ShipmentResponse response = shipmentService.createShipment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. Takip Numarasına Göre Kargo Sorgulama Uç Noktası
    @GetMapping("/{trackingNumber}")
    public ResponseEntity<ShipmentResponse> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        ShipmentResponse response = shipmentService.getShipmentByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(response);
    }

    // 3. Kargo Durumu Güncelleme Uç Noktası (State Pattern Tetikleyici)
    @PatchMapping("/{trackingNumber}/status")
    public ResponseEntity<ShipmentResponse> updateShipmentStatus(
            @PathVariable String trackingNumber,
            @Valid @RequestBody UpdateShipmentStatusRequest request) {

        ShipmentResponse response = shipmentService.updateShipmentStatus(trackingNumber, request);
        return ResponseEntity.ok(response);
    }
}
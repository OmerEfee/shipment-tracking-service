package shipmenttrackingservice.service;

import shipmenttrackingservice.dto.CreateShipmentRequest;
import shipmenttrackingservice.dto.ShipmentResponse;
import shipmenttrackingservice.dto.UpdateShipmentStatusRequest;

public interface ShipmentService {

    ShipmentResponse createShipment(CreateShipmentRequest request);

    ShipmentResponse getShipmentByTrackingNumber(String trackingNumber);

    ShipmentResponse updateShipmentStatus(String trackingNumber, UpdateShipmentStatusRequest request);
}
package shipmenttrackingservice.service.state;

import org.springframework.stereotype.Component;

import shipmenttrackingservice.enums.ShipmentStatus;

@Component
public class CreatedState implements ShipmentState {

    @Override
    public ShipmentStatus getStatus() {
        return ShipmentStatus.CREATED;
    }

    @Override
    public boolean canTransitionTo(ShipmentStatus nextStatus) {
        // Oluşturulmuş bir kargo sadece 'Yolda' veya 'İptal' durumuna geçebilir
        return nextStatus == ShipmentStatus.IN_TRANSIT || nextStatus == ShipmentStatus.CANCELLED;
    }
}
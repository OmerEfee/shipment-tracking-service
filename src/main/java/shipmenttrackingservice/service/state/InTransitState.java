package shipmenttrackingservice.service.state;

import org.springframework.stereotype.Component;

import shipmenttrackingservice.enums.ShipmentStatus;

@Component
public class InTransitState implements ShipmentState {

    @Override
    public ShipmentStatus getStatus() {
        return ShipmentStatus.IN_TRANSIT;
    }

    @Override
    public boolean canTransitionTo(ShipmentStatus nextStatus) {
        // Yoldaki kargo 'Dağıtımda' veya 'İptal' durumuna geçebilir
        return nextStatus == ShipmentStatus.OUT_FOR_DELIVERY || nextStatus == ShipmentStatus.CANCELLED;
    }
}
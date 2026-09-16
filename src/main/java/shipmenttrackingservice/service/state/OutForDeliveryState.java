package shipmenttrackingservice.service.state;

import org.springframework.stereotype.Component;

import shipmenttrackingservice.enums.ShipmentStatus;

@Component
public class OutForDeliveryState implements ShipmentState {

    @Override
    public ShipmentStatus getStatus() {
        return ShipmentStatus.OUT_FOR_DELIVERY;
    }

    @Override
    public boolean canTransitionTo(ShipmentStatus nextStatus) {
        // Dağıtımdaki kargo sadece 'Teslim Edildi' durumuna geçebilir
        return nextStatus == ShipmentStatus.DELIVERED;
    }
}
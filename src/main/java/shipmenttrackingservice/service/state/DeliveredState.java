package shipmenttrackingservice.service.state;

import org.springframework.stereotype.Component;

import shipmenttrackingservice.enums.ShipmentStatus;

@Component
public class DeliveredState implements ShipmentState {

    @Override
    public ShipmentStatus getStatus() {
        return ShipmentStatus.DELIVERED;
    }

    @Override
    public boolean canTransitionTo(ShipmentStatus nextStatus) {
        // Teslim edilmiş kargo artık başka hiçbir duruma geçemez (Terminal durum)
        return false;
    }
}
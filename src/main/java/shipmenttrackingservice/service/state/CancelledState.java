package shipmenttrackingservice.service.state;

import org.springframework.stereotype.Component;

import shipmenttrackingservice.enums.ShipmentStatus;

@Component
public class CancelledState implements ShipmentState {

    @Override
    public ShipmentStatus getStatus() {
        return ShipmentStatus.CANCELLED;
    }

    @Override
    public boolean canTransitionTo(ShipmentStatus nextStatus) {
        // İptal edilmiş bir kargo artık başka hiçbir duruma geçemez (Terminal durum)
        return false;
    }
}
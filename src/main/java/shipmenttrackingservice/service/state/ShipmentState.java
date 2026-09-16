package shipmenttrackingservice.service.state;

import shipmenttrackingservice.enums.ShipmentStatus;

public interface ShipmentState {

    // Bu durum sınıfının temsil ettiği enum değeri (Örn: CREATED, IN_TRANSIT)
    ShipmentStatus getStatus();

    // Bir sonraki duruma geçilebilir mi kontrolü (Doğruysa true, yanlışsa false)
    boolean canTransitionTo(ShipmentStatus nextStatus);
}
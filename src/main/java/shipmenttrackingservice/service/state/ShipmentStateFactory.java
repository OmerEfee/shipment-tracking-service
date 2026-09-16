package shipmenttrackingservice.service.state;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import shipmenttrackingservice.enums.ShipmentStatus;

@Component
public class ShipmentStateFactory {

    private final Map<ShipmentStatus, ShipmentState> stateMap;

    // Spring, ShipmentState arayüzünü uygulayan tüm @Component sınıflarını listeye otomatik enjekte eder
    public ShipmentStateFactory(List<ShipmentState> states) {
        this.stateMap = states.stream()
                .collect(Collectors.toMap(ShipmentState::getStatus, state -> state));
    }

    // İstenen duruma ait kural sınıfını O(1) hızında döndürür
    public ShipmentState getState(ShipmentStatus status) {
        ShipmentState state = stateMap.get(status);
        if (state == null) {
            throw new IllegalArgumentException("Tanımsız kargo durumu: " + status);
        }
        return state;
    }
}
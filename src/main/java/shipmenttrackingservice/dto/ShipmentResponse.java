package shipmenttrackingservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shipmenttrackingservice.enums.ShipmentStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {

    private String trackingNumber;
    private String senderName;
    private String receiverName;
    private String receiverAddress;
    private ShipmentStatus currentStatus;
    private String currentStatusDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ShipmentStatusLogResponse> history;
}
package shipmenttrackingservice.dto;

import java.time.LocalDateTime;

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
public class ShipmentStatusLogResponse {

    private ShipmentStatus status;
    private String statusDescription;
    private String location;
    private String comment;
    private LocalDateTime timestamp;
}
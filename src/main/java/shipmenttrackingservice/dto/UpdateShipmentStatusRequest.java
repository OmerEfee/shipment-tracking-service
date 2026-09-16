package shipmenttrackingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class UpdateShipmentStatusRequest {

    @NotNull(message = "Yeni kargo durumu belirtilmelidir")
    private ShipmentStatus newStatus;

    @NotBlank(message = "Konum bilgisi boş bırakılamaz")
    @Size(max = 150, message = "Konum bilgisi en fazla 150 karakter olabilir")
    private String location;

    @Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir")
    private String comment;
}
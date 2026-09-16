package shipmenttrackingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShipmentRequest {

    @NotBlank(message = "Gönderici adı boş bırakılamaz")
    @Size(min = 2, max = 100, message = "Gönderici adı 2 ile 100 karakter arasında olmalıdır")
    private String senderName;

    @NotBlank(message = "Alıcı adı boş bırakılamaz")
    @Size(min = 2, max = 100, message = "Alıcı adı 2 ile 100 karakter arasında olmalıdır")
    private String receiverName;

    @NotBlank(message = "Alıcı adresi boş bırakılamaz")
    @Size(min = 10, max = 500, message = "Alıcı adresi en az 10 karakter olmalıdır")
    private String receiverAddress;
}
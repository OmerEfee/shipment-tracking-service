package shipmenttrackingservice.enums;

public enum ShipmentStatus {
    CREATED("Kargo Kaydı Oluşturuldu"),
    IN_TRANSIT("Transfer Merkezinde / Yolda"),
    OUT_FOR_DELIVERY("Dağıtıma Çıkarıldı"),
    DELIVERED("Teslim Edildi"),
    CANCELLED("İptal Edildi");

    private final String description;

    ShipmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
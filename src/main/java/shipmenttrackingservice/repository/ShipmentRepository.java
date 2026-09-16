package shipmenttrackingservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shipmenttrackingservice.entity.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    // Takip numarasına göre kargo sorgulama
    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    // Takip numarası daha önce üretilmiş mi kontrolü (Çakışma kontrolü için)
    boolean existsByTrackingNumber(String trackingNumber);
}
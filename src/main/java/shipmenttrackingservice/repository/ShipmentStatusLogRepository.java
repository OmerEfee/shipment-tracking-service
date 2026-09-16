package shipmenttrackingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shipmenttrackingservice.entity.ShipmentStatusLog;

@Repository
public interface ShipmentStatusLogRepository extends JpaRepository<ShipmentStatusLog, Long> {

    // Belirli bir kargoya ait tarihçeyi yeniden eskiye doğru sıralı getirir
    List<ShipmentStatusLog> findByShipmentIdOrderByTimestampDesc(Long shipmentId);
}
package com.Inventory.repository;

import com.Inventory.enums.ReservationStatus;
import com.Inventory.model.ProductReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ProductReservation, Long> {

    List<ProductReservation> findByOrderId(Long orderId);
    List<ProductReservation> findByVariantIdIn(List<Long> variantIds);

    List<ProductReservation> findByStatusAndExpiresAtAfterAndVariantIdIn(
            ReservationStatus status, LocalDateTime now, List<Long> variantIds);


}

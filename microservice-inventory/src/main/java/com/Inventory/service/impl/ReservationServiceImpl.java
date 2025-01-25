package com.Inventory.service.impl;

import com.Inventory.client.OrderClient;
import com.Inventory.dto.OrderItemDTO;
import com.Inventory.enums.ReservationStatus;
import com.Inventory.model.ProductReservation;
import com.Inventory.repository.ReservationRepository;
import com.Inventory.service.HandleStockService;
import com.Inventory.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final HandleStockService stockService;
    private final OrderClient orderClient;

    @Override
    public List<ProductReservation> findReservationsActiveByVariants(List<Long> variantIds){
        return reservationRepository.findByStatusAndExpiresAtAfterAndVariantIdIn(ReservationStatus.RESERVED,
                LocalDateTime.now(), variantIds);
    }

    @Override
    public void createReservations(Long orderId, String token){
        List<OrderItemDTO> orderItems = orderClient.getOrderById(orderId, token);
        List<ProductReservation> reservations = orderItems.stream()
                        .map( orderItem -> ProductReservation.builder()
                                .variantId(orderItem.getProductVariantId())
                                .reservedQuantity(orderItem.getQuantity())
                                .orderId(orderId)
                                .status(ReservationStatus.RESERVED)
                                .expiresAt(LocalDateTime.now().plusMinutes(20))
                                .build()).collect(Collectors.toList());

            reservationRepository.saveAll(reservations);
    }

    @Transactional
    public void confirmReservations(Long orderId){
        List<ProductReservation> reservations = getReservationsByOrder(orderId);
        List<ProductReservation> modifiedReservations = reservations.stream().peek(r -> {
            if(r.getStatus().equals(ReservationStatus.RESERVED)){
                r.setStatus(ReservationStatus.CONFIRMED);
            }
        }).toList();

        stockService.reduceStock(extractQuantitiesById(reservations));
        reservationRepository.saveAll(modifiedReservations);
    }

    @Override
    public void cancelReservations(Long orderId){
        List<ProductReservation> reservations = getReservationsByOrder(orderId);
        reservations.forEach( r ->  r.setStatus(ReservationStatus.CANCELED));
        reservationRepository.saveAll(reservations);
    }

    @Override
    public void increaseReservationTime(Long orderId){
         List<ProductReservation> reservations = getReservationsByOrder(orderId);
         reservations.forEach(
                reservation -> reservation.setExpiresAt(LocalDateTime.now().plusHours(1))
        );

         reservationRepository.saveAll(reservations);
    }
    private List<ProductReservation> getReservationsByOrder(Long orderId){
        return reservationRepository.findByOrderId(orderId);
    }

    private Map<Long, Integer> extractQuantitiesById(List<ProductReservation> reservations){
        return reservations.stream().collect(Collectors.toMap(ProductReservation::getVariantId,
                ProductReservation::getReservedQuantity));
    }

}

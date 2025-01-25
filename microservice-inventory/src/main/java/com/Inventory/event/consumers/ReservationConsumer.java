package com.Inventory.event.consumers;

import com.Inventory.event.topics.ReservationTopics;
import com.Inventory.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationConsumer {

    private final ReservationService reservationService;
    private final ReservationTopics reservationTopics;

    @KafkaListener(topics = "#{reservationTopics.reservationCreated}", groupId = "${spring.kafka.consumer.group-id}")
    public void createReservation(ConsumerRecord<Long, String> record){
        reservationService.createReservations(record.key(), record.value());
    }

    @KafkaListener(topics = "#{reservationTopics.reservationConfirmed}", groupId = "${spring.kafka.consumer.group-id}")
    public void confirmReservation(ConsumerRecord<Long, String> record){
        reservationService.confirmReservations(record.key());
    }

    @KafkaListener(topics = "#{reservationTopics.reservationCanceled}", groupId = "${spring.kafka.consumer.group-id}")
    public void cancelReservation(ConsumerRecord<Long, String> record){
         Long orderId = record.key();
         reservationService.cancelReservations(orderId);
    }


    @KafkaListener(topics = "#{reservationTopics.reservationExtended}", groupId = "${spring.kafka.consumer.group-id}")
    public void increaseReservationTime(ConsumerRecord<Long, String> record){
        reservationService.increaseReservationTime(record.key());
    }

}
